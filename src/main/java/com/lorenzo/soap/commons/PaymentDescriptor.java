package com.lorenzo.soap.commons;

import java.io.Serializable;
import java.math.BigDecimal;

public class PaymentDescriptor implements Serializable {
	
	private static final long serialVersionUID = -5681554879719976992L;
	private String afiliation;
	private String authorization;
	private String direction;
	private String transactionDate;
	private String transactionHour;
	private BigDecimal amount;
	private String promotion;
	private String cardNumber;
	private String ownerCard;
	private String reference;
	private String message;
	private BigDecimal totalAmount;
	private String operationType;
	private String paymentType;
	private String issuer;
	
	public PaymentDescriptor() {
		super();
	}

	/**
	 * @return the afiliation
	 */
	public String getAfiliation() {
		return afiliation;
	}

	/**
	 * @param afiliation the afiliation to set
	 */
	public void setAfiliation(String afiliation) {
		this.afiliation = afiliation;
	}

	/**
	 * @return the authorization
	 */
	public String getAuthorization() {
		return authorization;
	}

	/**
	 * @param authorization the authorization to set
	 */
	public void setAuthorization(String authorization) {
		this.authorization = authorization;
	}

	/**
	 * @return the direction
	 */
	public String getDirection() {
		return direction;
	}

	/**
	 * @param direction the direction to set
	 */
	public void setDirection(String direction) {
		this.direction = direction;
	}

	/**
	 * @return the transactionDate
	 */
	public String getTransactionDate() {
		return transactionDate;
	}

	/**
	 * @param transactionDate the transactionDate to set
	 */
	public void setTransactionDate(String transactionDate) {
		this.transactionDate = transactionDate;
	}

	/**
	 * @return the transactionHour
	 */
	public String getTransactionHour() {
		return transactionHour;
	}

	/**
	 * @param transactionHour the transactionHour to set
	 */
	public void setTransactionHour(String transactionHour) {
		this.transactionHour = transactionHour;
	}

	/**
	 * @return the amount
	 */
	public BigDecimal getAmount() {
		return amount;
	}

	/**
	 * @param amount the amount to set
	 */
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	/**
	 * @return the promotion
	 */
	public String getPromotion() {
		return promotion;
	}

	/**
	 * @param promotion the promotion to set
	 */
	public void setPromotion(String promotion) {
		this.promotion = promotion;
	}

	/**
	 * @return the cardNumber
	 */
	public String getCardNumber() {
		return cardNumber;
	}

	/**
	 * @param cardNumber the cardNumber to set
	 */
	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}

	/**
	 * @return the ownerCard
	 */
	public String getOwnerCard() {
		return ownerCard;
	}

	/**
	 * @param ownerCard the ownerCard to set
	 */
	public void setOwnerCard(String ownerCard) {
		this.ownerCard = ownerCard;
	}

	/**
	 * @return the reference
	 */
	public String getReference() {
		return reference;
	}

	/**
	 * @param reference the reference to set
	 */
	public void setReference(String reference) {
		this.reference = reference;
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * @return the totalAmount
	 */
	public BigDecimal getTotalAmount() {
		return totalAmount;
	}

	/**
	 * @param totalAmount the totalAmount to set
	 */
	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}

	/**
	 * @return the operationType
	 */
	public String getOperationType() {
		return operationType;
	}

	/**
	 * @param operationType the operationType to set
	 */
	public void setOperationType(String operationType) {
		this.operationType = operationType;
	}

	/**
	 * @return the paymentType
	 */
	public String getPaymentType() {
		return paymentType;
	}

	/**
	 * @param paymentType the paymentType to set
	 */
	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}

	/**
	 * @return the issuer
	 */
	public String getIssuer() {
		return issuer;
	}

	/**
	 * @param issuer the issuer to set
	 */
	public void setIssuer(String issuer) {
		this.issuer = issuer;
	}

	@Override
	public String toString() {
		return "PaymentDescriptor [afiliation=" + afiliation + ", authorization=" + authorization + ", direction="
				+ direction + ", transactionDate=" + transactionDate + ", transactionHour=" + transactionHour
				+ ", amount=" + amount + ", promotion=" + promotion + ", cardNumber=" + cardNumber + ", ownerCard="
				+ ownerCard + ", reference=" + reference + ", message=" + message + ", totalAmount=" + totalAmount
				+ ", operationType=" + operationType + ", paymentType=" + paymentType + ", issuer=" + issuer + "]";
	}
}
