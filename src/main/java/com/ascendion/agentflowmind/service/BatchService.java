package com.ascendion.agentflowmind.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * BatchService is for Running the Bat file in the specified location
 * @author Nithyananda CV
 */
@Service
public class BatchService {

    private static final Logger logger = LoggerFactory.getLogger(BatchService.class);
    public int batchRunner(String filePath){
        Process process = null;
        ProcessBuilder processBuilder = null;

        try{
            logger.info("BatchService Started:: Filepath - "+filePath);
            processBuilder = new ProcessBuilder(filePath);
            processBuilder.redirectErrorStream(true);
            process = processBuilder.start();
            int returnCode = process.waitFor();
            logger.info("BatchService Completed:: ReturnCode - "+0);
            return 0;

        }catch(Exception e){
            logger.error("Error :: BatchService"+e.getMessage());
        }
        finally {
            if(process!=null)
                process.destroy();
            if(processBuilder != null)
                processBuilder = null;
        }
        return  2;
    }

}
