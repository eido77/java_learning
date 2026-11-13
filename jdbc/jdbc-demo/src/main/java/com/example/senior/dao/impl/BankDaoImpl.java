package com.example.senior.dao.impl;

import com.example.senior.dao.BankDao;
import com.example.senior.dao.BaseDao;

import java.sql.SQLException;

public class BankDaoImpl extends BaseDao implements BankDao {
    @Override
    public int addMoney(int id, int money) {
        try {
            String sql = "UPDATE t_bank SET money = money + ? WHERE id = ?";
            return executeUpdate(sql, money, id);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int subMoney(int id, int money) {
        try {
            String sql = "UPDATE t_bank SET money = money - ? WHERE id = ?";
            return executeUpdate(sql, money, id);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
