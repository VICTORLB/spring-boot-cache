package com.victor.simian.dto;

import com.victor.simian.model.BaseDomain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class DnaHumanSimianDtoV1 extends BaseDomain {


	private static final long serialVersionUID = -6763851089994196681L;


	@NotBlank(message = "{dna.not.blank}")
	private String[] dna;
	
}
