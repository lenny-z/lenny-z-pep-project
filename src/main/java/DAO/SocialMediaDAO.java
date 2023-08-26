package DAO;

import Model.Message;
import Util.ConnectionUtil;

import java.sql.*;

public class SocialMediaDAO {
    public Message insertMessage(Message message){
        Connection connection = ConnectionUtil.getConnection();

        try{
            String sql = "INSERT INTO "
        }
    }
}
