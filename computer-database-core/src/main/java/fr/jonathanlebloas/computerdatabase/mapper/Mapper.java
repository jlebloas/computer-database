package fr.jonathanlebloas.computerdatabase.mapper;

import java.util.List;

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
	public DTO toDTO(MODEL obj);

	/**
	 * Map from list model to its list DTO
	 *
	 * @param list
	 * @return
	 */
	public List<DTO> toDTO(List<MODEL> list);

	/**
	 * Map from dto to its model object
	 *
	 * @param dto
	 * @return
	 */
	public MODEL fromDTO(DTO dto);

	/**
	 * Map from list dto to its model list object
	 * 
	 * @param DTOList
	 * @return
	 */
	public List<MODEL> fromDTO(List<DTO> DTOList);
}
