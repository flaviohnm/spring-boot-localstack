package br.com.monteiro.s3.service;

import br.com.monteiro.s3.exception.FileListenerException;
import io.awspring.cloud.s3.S3PathMatchingResourcePatternResolver;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.WritableResource;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.S3Client;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class S3FileService {

    @Value("${s3Bucket}")
    private String directory;

    @Autowired
    private ResourceLoader resourceLoader;

    private ResourcePatternResolver resourcePatternResolver;

    @Autowired
    public void setupResolver(S3Client s3Client, ApplicationContext applicationContext) {
        this.resourcePatternResolver = new S3PathMatchingResourcePatternResolver(s3Client, applicationContext);
    }

    public boolean isFileExists(String file) {
        try {
            Resource resource = this.resourceLoader.getResource(String.format("s3://%s/%s", directory, file));
            return resource.contentLength() > 0;
        } catch (Exception ex) {
            return false;
        }
    }

    public List<Resource> searchFile(String name, boolean exact) {
        Resource[] resources = null;
        try {
            if (exact)
                resources = this.resourcePatternResolver.getResources(String.format("s3://%s/%s", directory, name));
            else
                resources = this.resourcePatternResolver.getResources(String.format("s3://%s/%s*.*", directory, name));
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
        }

        return Arrays.asList(resources)
                .stream().
                sorted(Comparator.comparing(Resource::getFilename))
                .collect(Collectors.toList());
    }

    public void saveFile(InputStream from, String to) throws FileListenerException {
        Resource resource = this.resourceLoader.getResource(String.format("s3://%s/%s", directory, to));
        WritableResource writableResource = (WritableResource) resource;

        try (OutputStream outputStream = writableResource.getOutputStream()) {
            from.transferTo(outputStream);
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            throw new FileListenerException(ex.getMessage(), ex);
        }
    }

    public String contentFile(String file) {
        try {
            Resource resource = resourceLoader.getResource(String.format("s3://%s/%s", directory, file));
            return new BufferedReader(
                    new InputStreamReader(resource.getInputStream(), StandardCharsets.ISO_8859_1))
                    .lines()
                    .collect(Collectors.joining("\n"));
        } catch (Exception ex) {
            return null;
        }
    }

}
