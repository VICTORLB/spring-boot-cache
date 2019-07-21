package com.victor.simian.businessimpl;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.victor.simian.business.DnaBO;
import com.victor.simian.dto.DnaDtoV1;
import com.victor.simian.dto.DnaHumanSimianDtoV1;
import com.victor.simian.dto.DnaResponse;
import com.victor.simian.dto.StatsDtoV1;
import com.victor.simian.model.Dna;
import com.victor.simian.repository.DnaRepository;
import com.victor.simian.util.DnaUtil;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
@AllArgsConstructor
public class DnaBOImpl implements DnaBO {

	private static final Logger LOGGER = LoggerFactory.getLogger(DnaBOImpl.class);

	private DnaRepository dnaRepository;

	public DnaResponse getDnas() {

		final List<Dna> foundDnas = dnaRepository.findAll();

		if (foundDnas == null) {
			return DnaResponse.builder().build();
		}
		return DnaResponse.builder().dna(foundDnas.stream().map(
				item -> DnaDtoV1.builder().id(item.getId()).dna(item.getDna()).isSimian(item.getIsSimian()).build())
				.collect(Collectors.toList())).build();

	}

	public StatsDtoV1 getStats() {

		final List<Dna> foundDnas = dnaRepository.findAll();

		Long totalDnas = foundDnas.stream().count();
		Long totalSimian = foundDnas.stream().filter(dnas -> dnas.getIsSimian().equals(Boolean.TRUE)).count();
		Long totalHuman = foundDnas.stream().filter(dnas -> dnas.getIsSimian().equals(Boolean.FALSE)).count();
		BigDecimal ratio = BigDecimal.valueOf(totalSimian).divide(BigDecimal.valueOf(totalHuman));
		LOGGER.info("count_mutant_dna [{}], count_human_dna[{}], ratio[{}], totalDnas[{}]", totalSimian, totalHuman,
				ratio, totalDnas);

		return StatsDtoV1.builder().countHumanDna(totalHuman).countMutantDna(totalSimian).ratio(ratio).build();

	}

	public DnaDtoV1 addDna(DnaHumanSimianDtoV1 dnaRequest) {
		Dna dna = dnaRepository.saveAndFlush(Dna.builder().dna(Arrays.toString(dnaRequest.getDna()))
				.isSimian(DnaUtil.isSimian(dnaRequest.getDna())).build());

		return DnaDtoV1.builder().dna(dna.getDna()).id(dna.getId()).isSimian(dna.getIsSimian()).build();
	}

}