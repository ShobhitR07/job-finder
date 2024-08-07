package com.embarkx.jobms.job.impl;


import com.embarkx.jobms.job.Job;
import com.embarkx.jobms.job.JobRepository;
import com.embarkx.jobms.job.JobService;
import com.embarkx.jobms.job.clients.CompanyClient;
import com.embarkx.jobms.job.clients.ReviewClient;
import com.embarkx.jobms.job.dto.JobWithCompanyDTO;
import com.embarkx.jobms.job.external.Company;
import com.embarkx.jobms.job.external.Review;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class JobServiceImpl implements  JobService {
   // private List<Job> jobs = new ArrayList<>();

    JobRepository jobRepository ;

    @Autowired
    RestTemplate restTemplate ;

    private CompanyClient companyClient ;

    private ReviewClient reviewClient ;

    public JobServiceImpl(JobRepository jobRepository,CompanyClient companyClient,ReviewClient reviewClient) {
        this.jobRepository = jobRepository;
        this.companyClient=companyClient ;
        this.reviewClient=reviewClient ;
    }



    @Override
    public List<JobWithCompanyDTO>findAll() {

        List<Job> jobs=jobRepository.findAll() ;
        List<JobWithCompanyDTO> jobWithCompanyDTOS=new ArrayList<>();
//        RestTemplate restTemplate=new RestTemplate() ;

        for (Job job: jobs) {



            JobWithCompanyDTO jobWithCompanyDTO=new JobWithCompanyDTO();
            jobWithCompanyDTO.setJob(job);
         //   Company company=restTemplate.getForObject("http://COMPANYMS:8081/companies/"+job.getCompanyId(), Company.class);
              Company company=companyClient.getCompany(job.getCompanyId()) ;
          //  ResponseEntity<List<Review>> responseEntity=restTemplate.exchange("http://REVIEWMS:8083/reviews?companyId=" + job.getCompanyId(), HttpMethod.GET, null, new ParameterizedTypeReference<List<Review>>() {
          //  }) ;
            List<Review> review=reviewClient.getReview(job.getCompanyId()) ;
            jobWithCompanyDTO.setCompany(company);
            jobWithCompanyDTO.setReview(review);
            jobWithCompanyDTOS.add(jobWithCompanyDTO) ;


        }

        return jobWithCompanyDTOS;
    }

    @Override
    public void createJob(Job job) {
        jobRepository.save(job);
    }

    public JobWithCompanyDTO getJobById(Long id){
        Job job=jobRepository.findById(id).orElse(null) ;
        if(job==null)return null ;
//        RestTemplate restTemplate=new RestTemplate() ;
        Company company=restTemplate.getForObject("http://COMPANYMS:8081/companies/"+job.getCompanyId(), Company.class);
        ResponseEntity<List<Review>> responseEntity=restTemplate.exchange("http://REVIEWMS:8083/reviews?companyId=" + job.getCompanyId(), HttpMethod.GET, null, new ParameterizedTypeReference<List<Review>>() {
        }) ;

        List<Review> review=responseEntity.getBody() ;

        JobWithCompanyDTO jobWithCompanyDTO=new JobWithCompanyDTO();
        jobWithCompanyDTO.setJob(job);
        jobWithCompanyDTO.setCompany(company);
        jobWithCompanyDTO.setReview(review);

        return jobWithCompanyDTO ;

    }

    public boolean deleteJobById(Long id){
        if(jobRepository.findById(id).isEmpty())return false ;
        jobRepository.deleteById(id);
        return true ;

    }

    @Override
    public boolean updateJob(Long id, Job updatedJob) {
        Optional<Job> jobOptional=jobRepository.findById(id) ;
        if (jobOptional.isPresent()){
            Job temp=jobOptional.get() ;
                temp.setDescription(updatedJob.getDescription());
                temp.setLocation(updatedJob.getLocation());
                temp.setTitle(updatedJob.getTitle());
                temp.setMaxSalary(updatedJob.getMaxSalary());
                temp.setMinSalary(updatedJob.getMinSalary());
                jobRepository.save(temp) ;
                return true ;

        }
        return false;
    }
}
