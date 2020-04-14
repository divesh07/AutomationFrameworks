package common.s3;

import java.io.File;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.CreateBucketRequest;
import com.amazonaws.services.s3.model.GetBucketLocationRequest;
import com.amazonaws.services.s3.transfer.Download;
import com.amazonaws.services.s3.transfer.MultipleFileDownload;
import com.amazonaws.services.s3.transfer.MultipleFileUpload;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.TransferManagerBuilder;
import com.amazonaws.services.s3.transfer.Upload;

public class S3DataUploader {

    private String awsKey = "QUtJQTZKT1RMWDc1SkNCT0pQVk8=";

    private String awsSecretKey = "anNqMWxzVUp6aGVqb2hPRm1SZmpvOWhkbE03amR4UUg1Z1pROTdmRA==";

    private String bucketName = "bhargav-test-bucket/security";

    private static String keyPath = "report";

    public S3DataUploader() {
        byte[] bytes = Base64.getDecoder().decode(awsKey);
        awsKey = new String(bytes);
        byte[] bytes1 = Base64.getDecoder().decode(awsSecretKey);
        awsSecretKey = new String(bytes1);
    }

    public AmazonS3 createConnection() {

        try {
            AWSCredentials credentials = new BasicAWSCredentials(awsKey, awsSecretKey);
            AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
                    .withCredentials(new AWSStaticCredentialsProvider(credentials))
                    .withRegion(Regions.US_WEST_2).build();

            if (!s3Client.doesBucketExistV2(bucketName)) {
                s3Client.createBucket(new CreateBucketRequest(bucketName));

            } else {
                System.out.println("Bucket already present");
            }
            return s3Client;

        } catch (AmazonServiceException e) {
            // The call was transmitted successfully, but Amazon S3 couldn't process
            // it and returned an error response.
            e.printStackTrace();
        } catch (SdkClientException e) {
            // Amazon S3 couldn't be contacted for a response, or the client
            // couldn't parse the response from Amazon S3.
            e.printStackTrace();
        }
        return null;
    }

    public void getBucketLocation(AmazonS3 s3Client) {
        // Verify that the bucket was created by retrieving it and checking its location.
        String bucketLocation = s3Client
                .getBucketLocation(new GetBucketLocationRequest(bucketName));
        System.out.println("Bucket location: " + bucketLocation);
    }

    public void listAllBuckets(AmazonS3 s3Client) {
        List<Bucket> buckets = s3Client.listBuckets();
        for (Bucket bucket : buckets) {
            System.out.println(bucket.getName());
        }
    }

    public void uploadfile(AmazonS3 s3Client, String filePath) {
        TransferManager transferManager = TransferManagerBuilder.standard().withS3Client(s3Client)
                .build();
        try {
            File file = new File(filePath);
            Upload upload = transferManager.upload(bucketName, file.getName(), file);
            System.out.println(upload.getProgress().getPercentTransferred());
            //upload.addProgressListener();
            upload.waitForCompletion();
            System.out.println(upload.getProgress().getPercentTransferred());
            System.out.println("done uploading file to s3 ");

        } catch (Exception e) {
            System.out.println("failed to load the file into s3." + e);
        }
        transferManager.shutdownNow();
    }

    public void uploadMultipleFiles(AmazonS3 s3Client, String[] file_paths) {
        TransferManager transferManager = TransferManagerBuilder.standard().withS3Client(s3Client)
                .build();
        ArrayList<File> files = new ArrayList<File>();
        for (String path : file_paths) {
            files.add(new File(path));
        }
        try {
            MultipleFileUpload upload = transferManager
                    .uploadFileList(bucketName, "", new File("."), files);
            System.out.println(upload.getProgress().getPercentTransferred());
            upload.waitForCompletion();
            System.out.println(upload.getProgress().getPercentTransferred());
            System.out.println("done uploading multiple files to s3");
        } catch (Exception e) {
            System.out.println("failed to upload multiple files to s3");
            e.printStackTrace();
        }
        transferManager.shutdownNow();
    }

    public void downloadFile(AmazonS3 s3Client, String key, String file_path) {
        File file = new File(file_path);
        TransferManager transferManager = TransferManagerBuilder.standard().withS3Client(s3Client)
                .build();
        try {
            Download download = transferManager.download(bucketName, key, file);
            download.waitForCompletion();
            System.out.println("done downloading files from s3");
        } catch (AmazonServiceException | InterruptedException e) {
            System.out.println("failed to download files from s3");
            System.err.println(e.getStackTrace());
        }
        transferManager.shutdownNow();
    }

    public void downloadDirectory(AmazonS3 s3Client, String key, String destDir) {
        TransferManager transferManager = TransferManagerBuilder.standard().withS3Client(s3Client)
                .build();
        bucketName += "/reports";
        MultipleFileDownload download = transferManager
                .downloadDirectory(bucketName, "/download", new File(destDir));
        try {
            download.waitForCompletion();
            System.out.println("done downloading dir from s3");
        } catch (InterruptedException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
        transferManager.shutdownNow();
    }

    public void uploadDirectory(AmazonS3 s3Client, String dir_path) {
        TransferManager transferManager = TransferManagerBuilder.standard().withS3Client(s3Client)
                .build();
        try {
            MultipleFileUpload upload = transferManager
                    .uploadDirectory(bucketName, "reports", new File(dir_path), false);

            System.out.println(upload.getProgress().getPercentTransferred());
            upload.waitForCompletion();
            System.out.println(upload.getProgress().getPercentTransferred());
            System.out.println("done uploading dir to s3");
        } catch (Exception e) {
            System.out.println("failed to load the files into s3." + e);
        }
        transferManager.shutdownNow();
    }

    public void shutdown(AmazonS3 s3Client) {
        TransferManager transferManager = TransferManagerBuilder.standard().withS3Client(s3Client)
                .build();
        transferManager.shutdownNow();
    }

    public static void main(String[] args) {
        String file_path = "/Users/divesh.gandhi/Documents/ZAP/champ1.xml";
        String dir_path = "/Users/divesh.gandhi/Documents/ZAP";
        String download_dir_path = "/Users/divesh.gandhi/Documents/ZAP/download";
        String[] file_paths = { "/Users/divesh.gandhi/Documents/ZAP/*.html" };
        String download_path = "/Users/divesh.gandhi/Documents/ZAP/download.xml";

        S3DataUploader dataUploader = new S3DataUploader();
        AmazonS3 s3Client = dataUploader.createConnection();
        dataUploader.getBucketLocation(s3Client);

        dataUploader.uploadfile(s3Client, file_path);

        //s3Client = dataUploader.createConnection();
        //dataUploader.uploadMultipleFiles(s3Client, file_paths);

        //s3Client = dataUploader.createConnection();
        //dataUploader.uploadDirectory(s3Client, dir_path);

        //s3Client = dataUploader.createConnection();
        //dataUploader.downloadFile(s3Client, "champ1.xml", download_path);

        //s3Client = dataUploader.createConnection();
        //dataUploader.downloadDirectory(s3Client, "/reports", download_dir_path);
        dataUploader.shutdown(s3Client);
    }

}
