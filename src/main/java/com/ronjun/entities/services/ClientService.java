package com.ronjun.entities.services;

import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ronjun.entities.Client;
import com.ronjun.entities.dto.ClientDTO;
import com.ronjun.entities.services.exceptions.DatabaseException;
import com.ronjun.exceptions.ClientNotFoundException;
import com.ronjun.repositories.ClientRepository;

@Service
public class ClientService {

	@Autowired
	private ClientRepository repository;

	@Transactional(readOnly = true)
	public Page<ClientDTO> findAllPaged(PageRequest pageable) {
		Page<Client> list = repository.findAll(pageable);
		return list.map(x -> new ClientDTO(x));
	}

	@Transactional(readOnly = true)
	public ClientDTO findById(Long id) {
		Optional<Client> obj = repository.findById(id);
		Client entity = obj.orElseThrow(() -> new ClientNotFoundException("Cliente não encontrado!"));
		return new ClientDTO(entity);
	}

	@Transactional
	public ClientDTO insert(ClientDTO dto) {
		Client entity = new Client();
		copyToEntity(entity, dto);
		repository.save(entity);
		return new ClientDTO(entity);
	}
	
	@Transactional
	public ClientDTO update(Long id, ClientDTO dto) {
		try {
			Client entity = repository.getOne(id);
			copyToEntity(entity, dto);
			repository.save(entity);
			return new ClientDTO(entity);
		} catch (EntityNotFoundException e) {
			throw new ClientNotFoundException("Id não encontrado");
		}
	}
	
	public void delete(Long id) {
		try {
			repository.deleteById(id);
		}
		catch(EmptyResultDataAccessException e) {
			throw new ClientNotFoundException("Id não encontrado: "+ id);
		}
		catch(DataIntegrityViolationException e) {
			throw new DatabaseException("Violação de integridade");
		}
	}
	private void copyToEntity(Client entity, ClientDTO dto) {
		entity.setName(dto.getName());
		entity.setBirthDate(dto.getBirthDate());
		entity.setCpf(dto.getCpf());
		entity.setChildren(dto.getChildren());
		entity.setIncome(dto.getIncome());
	}
}
