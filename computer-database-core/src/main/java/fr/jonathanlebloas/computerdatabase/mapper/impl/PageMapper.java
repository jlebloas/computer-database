package fr.jonathanlebloas.computerdatabase.mapper.impl;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Component;

import fr.jonathanlebloas.computerdatabase.dto.PageDTO;
import fr.jonathanlebloas.computerdatabase.mapper.Mapper;

@Component
public class PageMapper implements Mapper<PageRequest, PageDTO> {
	@Override
	public PageRequest fromDTO(PageDTO dto) {
		return new PageRequest(dto.getIndex(), dto.getSize(), Direction.valueOf(dto.getDirection()), dto.getField());
	}
}
