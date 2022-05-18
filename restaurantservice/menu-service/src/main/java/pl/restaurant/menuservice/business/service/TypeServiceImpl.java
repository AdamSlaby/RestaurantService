package pl.restaurant.menuservice.business.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.restaurant.menuservice.api.mapper.TypeMapper;
import pl.restaurant.menuservice.api.response.Type;
import pl.restaurant.menuservice.business.exception.type.TypeAlreadyExistsException;
import pl.restaurant.menuservice.business.exception.type.TypeNotFoundException;
import pl.restaurant.menuservice.data.entity.TypeEntity;
import pl.restaurant.menuservice.data.repository.TypeRepo;

import java.util.List;

@Service
@AllArgsConstructor
public class TypeServiceImpl implements TypeService {
    private TypeRepo typeRepo;

    @Override
    public List<Type> getAllTypes() {
        return typeRepo.getAllTypes();
    }

    @Override
    public Type addType(String type) {
        if (typeRepo.existsByName(type))
            throw new TypeAlreadyExistsException();
        return TypeMapper.mapDataToObject(typeRepo.save(new TypeEntity(type)));
    }

    @Override
    public void updateType(Integer typeId, String type) {
        if (typeRepo.existsByName(type))
            throw new TypeAlreadyExistsException();
        TypeEntity typeEntity = typeRepo.findById(typeId)
                .orElseThrow(TypeNotFoundException::new);
        typeEntity.setName(type);
        typeRepo.save(typeEntity);
    }
}
