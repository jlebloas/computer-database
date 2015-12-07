package fr.jonathanlebloas.computerdatabase.mapper;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper model / DTO
 *
 * @param <MODEL>
 * @param <DTO>
 */
public interface Mapper<MODEL, DTO> {

	/**
	 * Map from model to DTO
	 *
	 * @param obj
	 * @return
	 */
	public default DTO toDTO(MODEL obj) {
		throw new UnsupportedOperationException();
	}

	/**
	 * Map from list model to its list DTO
	 *
	 * @param list
	 * @return
	 */
	public default List<DTO> toDTO(List<MODEL> list) {
		return list.stream().map(this::toDTO).collect(Collectors.toList());
	}

	/**
	 * Map from dto to its model object
	 *
	 * @param dto
	 * @return
	 */
	public default MODEL fromDTO(DTO dto) {
		throw new UnsupportedOperationException();
	}

	/**
	 * Map from list dto to its model list object
	 *
	 * @param DTOList
	 * @return
	 */
	public default List<MODEL> fromDTO(List<DTO> dtoList) {
		return dtoList.stream().map(this::fromDTO).collect(Collectors.toList());
	}
}
