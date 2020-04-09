package battle.service.service;

import battle.service.dto.ParticipatingSubdivisionDto;
import battle.service.entity.ParticipatingSubdivision;
import battle.service.repository.ParticipatingSubdivisionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ParticipatingSubdivisionService {

    private final ParticipatingSubdivisionRepository participatingSubdivisionRepository;

    public ParticipatingSubdivision registerParticipant(ParticipatingSubdivisionDto dto) {
        ParticipatingSubdivision subdivision = new ParticipatingSubdivision();
        subdivision.setSubdivisionId(dto.getSubdivisionId());
        subdivision.setSubdivisionType(dto.getSubdivisionType());

        return participatingSubdivisionRepository.save(subdivision);
    }

    public List<ParticipatingSubdivision> getAllParticipant() {
        return participatingSubdivisionRepository.findAll();
    }

    public void deleteParticipantById(Integer id) {
        participatingSubdivisionRepository.deleteById(id);
    }

    public Integer getSubdivisionId(Integer participantId) {
        ParticipatingSubdivision maybeParticipant = participatingSubdivisionRepository.findById(participantId).orElseThrow(NoClassDefFoundError::new);

        return maybeParticipant.getSubdivisionId();
    }
}
