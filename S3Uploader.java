import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import java.nio.file.Path;

public class S3Uploader {
    private static final S3Client s3 = S3Client.builder().region(Region.US_EAST_1).build();
    public static void uploadFile(File file, String bucket, String key) {
        PutObjectRequest req = PutObjectRequest.builder()
            .bucket(bucket).key(key).build();
        s3.putObject(req, Path.of(file.getAbsolutePath()));
        System.out.println("Uploaded to S3: " + bucket + "/" + key);
    }
}
