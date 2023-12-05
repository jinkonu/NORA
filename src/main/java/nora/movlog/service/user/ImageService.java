package nora.movlog.service.user;

import lombok.RequiredArgsConstructor;
import nora.movlog.domain.user.Image;
import nora.movlog.domain.user.Post;
import nora.movlog.repository.user.ImageRepository;
import nora.movlog.repository.user.PostRepository;
import org.springframework.core.io.UrlResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
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



    /* CREATE */
    @Transactional
    public Image save(MultipartFile multipartFile, Post post) throws IOException {
        if (multipartFile.isEmpty()) return null;

        String originalFileName = multipartFile.getOriginalFilename();
        String savedFileName = UUID.randomUUID() + "." + extractExt(originalFileName);

        multipartFile.transferTo(new File(getFullPath(savedFileName)));

        return imageRepository.save(Image.builder()
                .originalFileName(originalFileName)
                .savedFileName(savedFileName)
                .post(post)
                .build());
    }

    private String extractExt(String originalFileName) {
        return originalFileName.substring(originalFileName.lastIndexOf(".") + 1);
    }


    /* READ */
    public ResponseEntity<UrlResource> download(long postId) throws MalformedURLException {
        Post post = postRepository.findById(postId).get();
        if (post.getImage() == null) return null;

        UrlResource url = new UrlResource("file:" + getFullPath(post.getImage().getSavedFileName()));

        return ResponseEntity.ok().body(url);
    }


    /* DELETE */
    @Transactional
    public void delete(Image image) throws IOException {
        imageRepository.delete(image);
        Files.deleteIfExists(Paths.get(getFullPath(image.getSavedFileName())));
    }
}
