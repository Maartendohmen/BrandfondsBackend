package nl.brandfonds.Brandfonds.model.responses;

import nl.brandfonds.Brandfonds.model.Receipt;

public class ReceiptFile {
    private Receipt receipt;
    private String Base64EncodedReceiptFile;

    public ReceiptFile(Receipt receipt, String receiptFile) {
        this.receipt = receipt;
        this.Base64EncodedReceiptFile = receiptFile;
    }

    public Receipt getReceipt() {
        return receipt;
    }
    public void setReceipt(Receipt receipt) {
        this.receipt = receipt;
    }

    public String getReceiptFile() {
        return Base64EncodedReceiptFile;
    }
    public void setReceiptFile(String receiptFile) {
        this.Base64EncodedReceiptFile = receiptFile;
    }
}
