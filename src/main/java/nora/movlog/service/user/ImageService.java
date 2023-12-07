package nora.movlog.service.user;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.RequiredArgsConstructor;
import nora.movlog.domain.user.Image;
import nora.movlog.domain.user.Post;
import nora.movlog.repository.user.ImageRepository;
import nora.movlog.repository.user.PostRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

import static nora.movlog.utils.FileUtility.getFullPath;

@RequiredArgsConstructor
@Service
public class ImageService {

    private final ImageRepository imageRepository;
    private final PostRepository postRepository;

    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;


    /* CREATE */
    @Transactional
    public Image save(MultipartFile multipartFile, Post post) throws IOException {
        if (multipartFile.isEmpty()) return null;

        String originalFileName = multipartFile.getOriginalFilename();
        String savedFileName = UUID.randomUUID() + "." + extractExt(originalFileName);

        amazonS3.putObject(bucket, originalFileName, multipartFile.getInputStream(), getMetadataFrom(multipartFile));
        return imageRepository.save(Image.builder()
                .originalFileName(originalFileName)
                .savedFileName(savedFileName)
                .post(post)
                .build());
    }

    private ObjectMetadata getMetadataFrom(MultipartFile multipartFile) {
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(multipartFile.getSize());
        objectMetadata.setContentType(multipartFile.getContentType());

        return objectMetadata;
    }

    private String extractExt(String originalFileName) {
        return originalFileName.substring(originalFileName.lastIndexOf(".") + 1);
    }


    /* READ */
    public ResponseEntity<UrlResource> download(long postId) {
        Post post = postRepository.findById(postId).get();
        if (post.getImage() == null) return null;

        String originalFileName = post.getImage().getOriginalFileName();
        UrlResource urlResource = new UrlResource(amazonS3.getUrl(bucket, originalFileName));
        String contentDisposition = "attachment; filename=\"" + originalFileName + "\"";

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition)
                .body(urlResource);
    }

    public String getImageUrl(String fileName) {
        return amazonS3.getUrl(bucket, fileName).toString();
    }


    /* DELETE */
    @Transactional
    public void delete(Image image) throws IOException {
        imageRepository.delete(image);
        Files.deleteIfExists(Paths.get(getFullPath(image.getSavedFileName())));
    }
}
