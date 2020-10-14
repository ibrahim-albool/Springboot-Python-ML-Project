package com.teach.predict.MLAPIs;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.io.CharStreams;
import com.teach.predict.web.rest.errors.BadRequestAlertException;
import feign.Response;
import feign.codec.ErrorDecoder;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Reader;

public class MLErrorDecoder implements ErrorDecoder {

    private final ErrorDecoder errorDecoder = new Default();

    @Override
    public Exception decode(String s, Response response) {

        String message = null;
        Reader reader = null;

        try {
            reader = response.body().asReader();
            //Easy way to read the stream and get a String object
            String result = CharStreams.toString(reader);
            //use a Jackson ObjectMapper to convert the Json String into a
            //Pojo
            ObjectMapper mapper = new ObjectMapper();
            //just in case you missed an attribute in the Pojo
            mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
            //init the Pojo
            ExceptionMessage exceptionMessage = mapper.readValue(result,
                ExceptionMessage.class);

            message = exceptionMessage.message;

        } catch (IOException e) {

            e.printStackTrace();
        }finally {

            //It is the responsibility of the caller to close the stream.
            try {

                if (reader != null)
                    reader.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        switch (response.status()) {

            case 404:
                return new FileNotFoundException(message == null ? "File no found" :
                    message);
            case 403:
                return new BadRequestAlertException(message == null ? "Forbidden " +
                    "access" : message,"","");

        }

        return errorDecoder.decode(s, response);
    }

    public static class ExceptionMessage{

        private String timestamp;
        private int status;
        private String error;
        private String message;
        private String path;

    }

}
