package com.embarkx.jobms.job;


import com.embarkx.jobms.job.dto.JobWithCompanyDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/jobs")
public class JobController {

   private JobService jobService ;

    public JobController(JobService jobService) {
        this.jobService = jobService;
    }

    @GetMapping
    public ResponseEntity<List<JobWithCompanyDTO>> findAll(){
        return new ResponseEntity<>(jobService.findAll(),HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<String> createJob(@RequestBody Job job){


        jobService.createJob(job);
        return new ResponseEntity<>("Job added successfully!",HttpStatus.OK) ;
    }

    @GetMapping("/{id}")
    public ResponseEntity<JobWithCompanyDTO> getJobById(@PathVariable Long id){
        JobWithCompanyDTO jobWithCompanyDTO=jobService.getJobById(id) ;
        if(jobWithCompanyDTO!=null)return new ResponseEntity<>(jobWithCompanyDTO, HttpStatus.OK) ;
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteJobById(@PathVariable Long id){
        boolean job=jobService.deleteJobById(id) ;
        if(job)return new ResponseEntity<>("job deleted successfull!",HttpStatus.OK) ;
        return new ResponseEntity<>("job not found!",HttpStatus.NOT_FOUND) ;
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateJob(@PathVariable Long id,@RequestBody Job updatedJob){
        boolean updated=jobService.updateJob(id,updatedJob);
        if(updated)return new ResponseEntity<>("update successful!",HttpStatus.OK) ;

        return new ResponseEntity<>("no job found",HttpStatus.NOT_FOUND) ;
    }



}
