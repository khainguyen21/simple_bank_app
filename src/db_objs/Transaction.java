package db_objs;

/*
    Transaction entity usd to store transaction data
 */

import javax.xml.crypto.Data;
import java.math.BigDecimal;
import java.util.Date;

public class Transaction {
    private final int userId;
    private final String transactionType;
    private final BigDecimal transactionAmount;
    private final Date transactionDate;


    public int getUserId() {
        return userId;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public BigDecimal getTransactionAmount() {
        return transactionAmount;
    }

    public Date getTransactionDate() {
        return transactionDate;
    }

    public Transaction(int userId, String transactionType, BigDecimal transactionAmount, Date transactionDate)
    {
        this.userId = userId;
        this.transactionType = transactionType;
        this.transactionAmount = transactionAmount;
        this.transactionDate = transactionDate;
    }

}
