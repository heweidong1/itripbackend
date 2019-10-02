package cn.kgc.itrip.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.core.convert.converter.Converter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class JsonMapper extends ObjectMapper
{
    public JsonMapper()
    {
        //给json重新定制规则
        this.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS,false);
        this.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
    }
}
