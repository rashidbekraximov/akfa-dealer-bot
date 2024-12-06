package uz.duol.akfadealerbot.service;

import java.io.File;

public interface ImageWriterService {

    File writeImage(String fileName,String value) throws Exception;

}
