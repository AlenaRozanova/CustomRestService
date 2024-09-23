package com.example.service;

import com.example.data.dao.BankDao;
import com.example.data.model.BankEntity;
import com.example.exception.BadRequestException;
import com.example.exception.BankNotFoundException;
import com.example.mapper.BankMapper;
import com.example.requset.BankInsertRequest;
import com.example.requset.BankModifyRequest;
import com.example.response.BankResponse;
import com.example.validator.BankValidator;
import com.google.inject.Inject;

/**
 * Business logic linked to banks
 */
public class BankServiceImpl implements BankService {
    private final BankDao bankDao;
    private final BankMapper bankMapper;
    private final BankValidator bankValidator;

    /**
     * Constructor for initializing the dependencies.
     *
     * @param bankDao       the database access layer for banks
     * @param bankValidator the validator for bank operations
     * @param bankMapper    the mapper for converting between domain and DTO objects
     */
    @Inject
    public BankServiceImpl(final BankDao bankDao, final BankValidator bankValidator,
                           final BankMapper bankMapper) {
        this.bankDao = bankDao;
        this.bankMapper = bankMapper;
        this.bankValidator = bankValidator;
    }

    /**
     * Adds a new bank to the database.
     *
     * @param bankInsertRequest the request containing the details of the new bank
     * @throws BadRequestException if the request is invalid
     */
    @Override
    public void addBank(final BankInsertRequest bankInsertRequest) {
        if (!bankValidator.validate(bankInsertRequest)) {
            throw new BadRequestException("Invalid bank insert request");
        }
        bankDao.insertBank(bankMapper.bankInsertRequestBankEntity(bankInsertRequest));
    }

    /**
     * Retrieves a bank from the database by its ID.
     *
     * @param id the ID of the bank to retrieve
     * @return the bank with the specified ID, or throws an exception if not found
     * @throws BankNotFoundException if the bank with the specified ID is not found
     */
    @Override
    public BankResponse getBankById(final int id) {
        BankEntity bankEntity = bankDao.selectBankById(id).orElseThrow(() -> new BankNotFoundException(id));
        return bankMapper.bankEntityToBankResponse(bankEntity);
    }

    /**
     * Updates an existing bank in the database.
     *
     * @param bankModifyRequest the request containing the details of the updated bank
     * @param id                the ID of the bank to update
     * @throws BadRequestException if the request is invalid
     */
    @Override
    public void updateBank(final BankModifyRequest bankModifyRequest, final int id) {
        if (!bankValidator.validate(bankModifyRequest, id)) {
            throw new BadRequestException("Invalid bank modify request");
        }
        bankDao.updateBank(bankMapper.bankModifyRequestToBankEntity(bankModifyRequest), id);
    }

    /**
     * Deletes a bank from the database by its ID.
     *
     * @param id the ID of the bank to delete
     */
    @Override
    public void deleteBankById(final int id) {
        bankDao.deleteBankById(id);
    }
}
