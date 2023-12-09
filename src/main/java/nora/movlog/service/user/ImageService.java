package nora.movlog.service.user;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.RequiredArgsConstructor;
import nora.movlog.domain.user.Image;
import nora.movlog.domain.user.Member;
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
import java.util.UUID;

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
    public Image savePostImage(MultipartFile multipartFile, Post post) throws IOException {
        return save(multipartFile, post, null);
    }

    @Transactional
    public Image saveProfiePicImage(MultipartFile multipartFile, Member member) throws IOException {
        return save(multipartFile, null, member);
    }

    @Transactional
    private Image save(MultipartFile multipartFile, Post post, Member member) throws IOException {
        if (multipartFile.isEmpty()) return null;

        String originalFileName = multipartFile.getOriginalFilename();
        String savedFileName = getSavedFileNameFrom(originalFileName);

        amazonS3.putObject(bucket, savedFileName, multipartFile.getInputStream(), getMetadataFrom(multipartFile));
        return imageRepository.save(Image.builder()
                .originalFileName(originalFileName)
                .savedFileName(savedFileName)
                .post(post)
                .member(member)
                .build());
    }

    private String getSavedFileNameFrom(String originalFileName) {
        return UUID.randomUUID() + "." + extractExt(originalFileName);
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

        String savedFileName = post.getImage().getSavedFileName();
        UrlResource urlResource = new UrlResource(amazonS3.getUrl(bucket, savedFileName));
        String contentDisposition = "attachment; filename=\"" + savedFileName + "\"";

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition)
                .body(urlResource);
    }

    public String getImageUrl(Image image) {
        return amazonS3.getUrl(bucket, image.getSavedFileName()).toString();
    }


    /* DELETE */
    @Transactional
    public void delete(Image image) {
        amazonS3.deleteObject(bucket, image.getSavedFileName());
        imageRepository.delete(image);
    }
}
