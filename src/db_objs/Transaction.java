package db_objs;

/*
    Transaction entity usd to store transaction data
 */

import javax.xml.crypto.Data;
import java.math.BigDecimal;
import java.util.Date;

public class Transaction {
    private final int userId;
    private final String transcationType;
    private final BigDecimal transactionAmount;
    private final Date transactionDate;


    public int getUserId() {
        return userId;
    }

    public String getTranscationType() {
        return transcationType;
    }

    public BigDecimal getTransactionAmount() {
        return transactionAmount;
    }

    public Date getTransactionDate() {
        return transactionDate;
    }

    public Transaction(int userId, String transcationType, BigDecimal transactionAmount, Date transactionDate)
    {
        this.userId = userId;
        this.transcationType = transcationType;
        this.transactionAmount = transactionAmount;
        this.transactionDate = transactionDate;
    }

}
