package com.sema4genomics.pbg;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.policy.Policy;
import com.amazonaws.auth.policy.Statement;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.BucketPolicy;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {


        String policy_text = null;
        Policy policy = null;

        final AmazonS3 s3 = AmazonS3ClientBuilder.defaultClient();
        try {
            ObjectMapper mapper = new ObjectMapper();
            BucketPolicy bucket_policy = s3.getBucketPolicy("s4-dgo-sequencer-marvin-useast1");

            policy_text = bucket_policy.getPolicyText();




            Object json = mapper.readValue(policy_text, Object.class);
            System.out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(json));

            policy = Policy.fromJson(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(json));

            /*
              Statement statement = new Statement(Effect.Allow);
    statement.setActions(Arrays.asList(actions));
    String resource = "arn:aws:s3:::" + bucketName;
    if (objectKey != null)
    {
        resource += "/" + objectKey;
    }
    statement.setResources(Arrays.asList(new Resource(resource)));
    policy.getStatements().add(statement);

             */
            Statement statement = new Statement(Statement.Effect.Allow);

            policy.getStatements().add( statement);

            String newJson  = policy.toJson() ;

            s3.setBucketPolicy("s4-dgo-sequencer-marvin-useast1", newJson);



        } catch (AmazonServiceException e) {
            System.err.println(e.getErrorMessage());
            System.exit(1);
        }
        catch ( Exception ex ){
            System.err.println(ex.getMessage());
            System.exit(1);

        }



    }
}
