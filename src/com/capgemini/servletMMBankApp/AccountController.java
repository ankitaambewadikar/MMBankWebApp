package com.capgemini.servletMMBankApp;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.moneymoney.account.SavingsAccount;
import com.moneymoney.account.service.SavingsAccountService;
import com.moneymoney.account.service.SavingsAccountServiceImpl;
import com.moneymoney.account.util.DBUtil;
import com.moneymoney.exception.AccountNotFoundException;

/**
 * Servlet implementation class AccountController
 */
@WebServlet("*.mm")
public class AccountController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	SavingsAccountService savingsAccountService = new SavingsAccountServiceImpl();
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AccountController() {
        super();
       
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String path = request.getServletPath();

		switch(path) 
		{
			case "/createNewAccount.mm":
				response.sendRedirect("createNewAccount.html");
				//System.out.println(request.getServletPath());
			break;
			
			case "/addNewAccount.mm":{
				String accoutHolderName = request.getParameter("accountHolderName");
				double accountBalance = Double.parseDouble(request.getParameter("accountBalance"));
				boolean salary = request.getParameter("isSalariedtrue").equalsIgnoreCase("Yes") ? true : false;
				System.out.println(accoutHolderName);
				System.out.println(accountBalance);
				System.out.println(salary);
				
				try {
					savingsAccountService.createNewAccount(accoutHolderName, accountBalance, salary);
				} catch (ClassNotFoundException | SQLException e) {
					e.printStackTrace();
				}
			}break;
			
			case "/closeAccount.mm":
				response.sendRedirect("deleteAccount.html");
				break;
			case "/deleteAccount.mm":{
				int closeAccountNumber =Integer.parseInt( request.getParameter("accountNumber"));
				System.out.println(closeAccountNumber);
				try {
					SavingsAccount closeSavingAccount = savingsAccountService
							.deleteAccount(closeAccountNumber);
					DBUtil.commit();
				} catch (ClassNotFoundException | AccountNotFoundException
						| SQLException e) {
					e.printStackTrace();
				}
			}break;
			
			case "/getCurrentBalance.mm":
				response.sendRedirect("getCurrentBalance.html");
			break;
			
			case "/currentBalance.mm":{
				int accountNumber =Integer.parseInt( request.getParameter("accountNumber"));
				try {
					double checkCurrentBalanceOfAccountNumber = savingsAccountService
							.checkCurrentBalance(accountNumber);
					System.out.println("current Balance is: "
							+ checkCurrentBalanceOfAccountNumber);
					PrintWriter out = response.getWriter();
					out.println("Current Balance of account Number"+ " "+accountNumber+ "is" +checkCurrentBalanceOfAccountNumber);
				} catch (ClassNotFoundException | SQLException
						| AccountNotFoundException e) {
					e.printStackTrace();
				}
			}break;
			
			case "/deposit.mm":
				response.sendRedirect("depositAmount.html");
			break;
			
			case "/depositAmountInAccount.mm":{
				int accountNumber =Integer.parseInt( request.getParameter("accountNumber"));
				double amount = Double.parseDouble(request.getParameter("amounttodeposit"));
				System.out.println(amount);
				try {
					SavingsAccount savingsAccount = savingsAccountService.getAccountById(accountNumber);
					savingsAccountService.deposit(savingsAccount, amount);
					DBUtil.commit();
				} catch (ClassNotFoundException | SQLException e) {
					e.printStackTrace();
					try {
						DBUtil.rollback();
					} catch (SQLException e1) {
						e1.printStackTrace();
					}
				} catch (Exception e) {
					try {
						DBUtil.rollback();
					} catch (SQLException e1) {
						e1.printStackTrace();
					}
				}
			}break;
			
			case "/withdraw.mm":
				response.sendRedirect("withdrawAmount.html");
			break;
			
			case "/withdrawAmountFromAccount.mm":{
				int accountNumber = Integer.parseInt( request.getParameter("accountNumber"));
				double amount = Double.parseDouble(request.getParameter("amounttowithdraw"));
				try {
					SavingsAccount savingsAccount = savingsAccountService
							.getAccountById(accountNumber);
					savingsAccountService.withdraw(savingsAccount, amount);
					DBUtil.commit();
				} catch (ClassNotFoundException | SQLException
						| AccountNotFoundException e) {
					try {
						DBUtil.rollback();
					} catch (SQLException e1) {
						e1.printStackTrace();
					}
					e.printStackTrace();
				} catch (Exception e) {
					try {
						DBUtil.rollback();
					} catch (SQLException e1) {
						e1.printStackTrace();
					}
				}
			}break;
			
			case "/fundTransfer.mm":
				response.sendRedirect("fundTransfer.html");
			break;
			
			case "/fund.mm":{
				int senderAccountNumber = Integer.parseInt( request.getParameter("senderAccountNumber"));
				int receiverAccountNumber = Integer.parseInt( request.getParameter("receiverAccountNumber"));
				double amount = Double.parseDouble(request.getParameter("amountToTransfer"));
				try {
					SavingsAccount senderSavingsAccount = savingsAccountService
							.getAccountById(senderAccountNumber);
					SavingsAccount receiverSavingsAccount = savingsAccountService
							.getAccountById(receiverAccountNumber);
					savingsAccountService.fundTransfer(senderSavingsAccount,
							receiverSavingsAccount, amount);
				} catch (ClassNotFoundException | SQLException e) {
					e.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}break;
				
		default:
			break;
		}
		}

		
		
	

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
	}

}
