package com.shivam.sv.service;

import com.shivam.sv.exception.ResourceNotFoundException;
import com.shivam.sv.model.Room;
import com.shivam.sv.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService{
    private final  RoomRepository roomRepository;
    @Override
    public Room addNewRoom(MultipartFile photo, String roomType, BigDecimal roomPrice) throws IOException, SQLException {
        Room room=new Room();

        room.setRoomType(roomType);
        room.setRoomPrice(roomPrice);

        if(!photo.isEmpty()){
            byte[] photoytes=photo.getBytes();
            Blob photoBlob=new  SerialBlob(photoytes);

            room.setPhoto(photoBlob);
        }

        return roomRepository.save(room);
    }

    @Override
    public List<String> getAllRoomTypes() {
        return roomRepository.finDistinctRoomTypes();
    }

    @Override
    public List<Room> getAllRooms() {
        return roomRepository.findAll();
    }

    @Override
    public byte[] getRoomPhotoById(Long roomId) throws SQLException {
        Optional<Room> theRoom=roomRepository.findById(roomId);
        if (theRoom.isEmpty()){
            throw new ResourceNotFoundException("Sorry, room not found");
        }
        Blob photoBlob=theRoom.get().getPhoto();
        if(photoBlob!=null){
            return photoBlob.getBytes(1,(int)photoBlob.length());
        }
        return null;
    }
}
