package com.n26.endpoints;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.n26.exception.OutdatedTransactionException;
import com.n26.model.Transaction;
import com.n26.model.TransactionStatistics;
import com.n26.service.TransactionService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("/transactionsStatistics")
@Api(tags = "Transaction statistics service", produces = "Get the current time, currency conversion, country code for a vat number")
public class TransactionStatisticsResource
{
	//protected static final Response.Status RESPONSE_STATUS_SUCCESS = Response.Status.CREATED;
	//protected static final Response.Status RESPONSE_STATUS_OUTDATED_TRANSACTION = Response.Status.NO_CONTENT;

	@Autowired
	TransactionService transactionService;

	@ApiOperation(value = "Get the transaction statistics")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Current time sucess") })
	@GetMapping
	///@Loggable
	@ResponseBody
	public ResponseEntity<TransactionStatistics> getTransactionStatistics() {
		transactionService.getTransactionStatistics();
		return ResponseEntity.ok().build();
	}
	
}

