package jobhunter.jobhunter.service;

import jobhunter.jobhunter.dto.skill.SkillConverter;
import jobhunter.jobhunter.domain.Skill;
import jobhunter.jobhunter.dto.skill.SkillRequest;
import jobhunter.jobhunter.dto.response.Meta;
import jobhunter.jobhunter.dto.response.ResultPagination;
import jobhunter.jobhunter.dto.skill.SkillResponse;
import jobhunter.jobhunter.exception.AppException;
import jobhunter.jobhunter.exception.ErrorCode;
import jobhunter.jobhunter.repository.SkillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SkillService {

    @Autowired
    private SkillRepository skillRepository;

    @Autowired
    private SkillConverter skillConverter;

    public SkillResponse handleCreateSkill(SkillRequest skillRequest){
        Optional<Skill> optionalSkill = skillRepository.findByName(skillRequest.getName());
        if(optionalSkill.isPresent()){
            throw new AppException(ErrorCode.USERNAME_EXISTED);
        }
        Skill skill = skillConverter.toSkill(skillRequest);

        return skillConverter.toSkillResponse(skillRepository.save(skill));
    }

    public SkillResponse handleUpdateSkill(SkillRequest skillRequest){
        Optional<Skill> optionalSkill = skillRepository.findById(skillRequest.getId());
        if(!optionalSkill.isPresent()){
            throw new AppException(ErrorCode.ID_NOT_FOUND);
        }
        Skill skill = optionalSkill.get();
        skill.setName(skillRequest.getName());
        skillRepository.save(skill);
        return skillConverter.toSkillResponse(skill);
    }

    public ResultPagination handleGetSkills(Specification<Skill> spec, Pageable pageable){
        Page<Skill> skillPage = skillRepository.findAll(spec, pageable);
        List<SkillResponse> skillResponses = skillPage.getContent().stream().map(skill -> skillConverter.toSkillResponse(skill))
                .toList();
        return ResultPagination.builder()
                .meta(Meta.builder()
                        .page(pageable.getPageNumber())
                        .pageSize(pageable.getPageSize())
                        .total(skillPage.getTotalElements())
                        .pages(skillPage.getTotalPages())
                        .build())
                .result(skillResponses)
                .build();
    }


    public void deleteSkill(long id) {
        // delete job (inside job_skill table)
        Optional<Skill> skillOptional = this.skillRepository.findById(id);
        Skill currentSkill = skillOptional.get();
        currentSkill.getJobs().forEach(job -> job.getSkills().remove(currentSkill));

        // delete subscriber (inside subscriber_skill table)
        currentSkill.getSubscribers().forEach(subs -> subs.getSkills().remove(currentSkill));

        // delete skill
        this.skillRepository.delete(currentSkill);
    }

}
