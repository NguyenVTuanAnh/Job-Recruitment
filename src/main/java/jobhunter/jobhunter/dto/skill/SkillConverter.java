package jobhunter.jobhunter.dto.skill;

import jobhunter.jobhunter.domain.Skill;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SkillConverter {

    @Autowired
    private ModelMapper modelMapper;

    public Skill toSkill(SkillRequest skillRequest){
        return modelMapper.map(skillRequest, Skill.class);
    }

    public SkillResponse toSkillResponse(Skill skill){
        return modelMapper.map(skill, SkillResponse.class);
    }
}
