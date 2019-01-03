package com.capgemini.servletMMBankApp;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.RequestDispatcher;
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
	private SavingsAccountServiceImpl savingsAccountService;
	private RequestDispatcher dispatcher;
	private boolean sort = false;
	int result;
	int flag = 0;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public AccountController() {
		super();

	}

	@Override
	public void init() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection connection = DriverManager.getConnection(
					"jdbc:mysql://localhost:3306/bankapp_db", "root", "root");
			PreparedStatement preparedStatement = connection
					.prepareStatement("DELETE FROM ACCOUNT");
			// preparedStatement.execute();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		savingsAccountService = new SavingsAccountServiceImpl();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String path = request.getServletPath();

		switch (path) {
		case "/createNewAccount.mm":
			response.sendRedirect("createNewAccount.jsp");
			break;

		case "/addNewAccount.mm": {
			String accoutHolderName = request.getParameter("accountHolderName");
			double accountBalance = Double.parseDouble(request
					.getParameter("accountBalance"));
			boolean salary = request.getParameter("isSalaried")
					.equalsIgnoreCase("Yes") ? true : false;
			/*System.out.println(accoutHolderName);
			System.out.println(accountBalance);
			System.out.println(salary);
*/
			try {
				savingsAccountService.createNewAccount(accoutHolderName,
						accountBalance, salary);
				PrintWriter out = response.getWriter();
				out.println("Account created successfully");
				// response.sendRedirect("getAllForm.mm");
			} catch (ClassNotFoundException | SQLException e) {
				e.printStackTrace();
			}
		}
			break;

		case "/closeAccount.mm":
			response.sendRedirect("deleteAccount.jsp");
			break;
		case "/deleteAccount.mm": {
			int closeAccountNumber = Integer.parseInt(request
					.getParameter("accountNumber"));
			System.out.println(closeAccountNumber);
			try {
				SavingsAccount closeSavingAccount = savingsAccountService
						.deleteAccount(closeAccountNumber);
				DBUtil.commit();
				response.sendRedirect("getAllForm.mm");
			} catch (ClassNotFoundException | AccountNotFoundException
					| SQLException e) {
				e.printStackTrace();
			}
		}
			break;

		case "/getCurrentBalance.mm":
			response.sendRedirect("searchByAccountNumber.jsp");
			break;

		case "/currentBalance.mm": {
			int accountNumber = Integer.parseInt(request
					.getParameter("accountNumber"));
			try {
				double checkCurrentBalanceOfAccountNumber = savingsAccountService
						.checkCurrentBalance(accountNumber);
				response.sendRedirect("searchByAccNumber.mm");
				System.out.println("current Balance is: "
						+ checkCurrentBalanceOfAccountNumber);
				PrintWriter out = response.getWriter();
				out.println("Current Balance of account Number" + " "
						+ accountNumber + "is"
						+ checkCurrentBalanceOfAccountNumber);
			} catch (ClassNotFoundException | SQLException
					| AccountNotFoundException e) {
				e.printStackTrace();
			}
		}
			break;

		case "/deposit.mm":
			response.sendRedirect("depositAmount.jsp");
			break;

		case "/depositAmountInAccount.mm": {
			int accountNumber = Integer.parseInt(request
					.getParameter("accountNumber"));
			double amount = Double.parseDouble(request
					.getParameter("amounttodeposit"));
			System.out.println(amount);
			try {
				SavingsAccount savingsAccount = savingsAccountService
						.getAccountById(accountNumber);
				savingsAccountService.deposit(savingsAccount, amount);
				// response.sendRedirect("getAllForm.mm");
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
		}
			break;

		case "/withdraw.mm":
			response.sendRedirect("withdrawAmount.jsp");
			break;

		case "/withdrawAmountFromAccount.mm": {
			int accountNumber = Integer.parseInt(request
					.getParameter("accountNumber"));
			double amount = Double.parseDouble(request
					.getParameter("amounttowithdraw"));
			try {
				SavingsAccount savingsAccount = savingsAccountService
						.getAccountById(accountNumber);
				savingsAccountService.withdraw(savingsAccount, amount);
				DBUtil.commit();
				// response.sendRedirect("getAllForm.mm");
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
		}
			break;

		case "/fundTransfer.mm":
			response.sendRedirect("fundTransfer.jsp");
			break;

		case "/fund.mm": {
			int senderAccountNumber = Integer.parseInt(request
					.getParameter("senderAccountNumber"));
			int receiverAccountNumber = Integer.parseInt(request
					.getParameter("receiverAccountNumber"));
			double amount = Double.parseDouble(request
					.getParameter("amountToTransfer"));
			try {
				SavingsAccount senderSavingsAccount = savingsAccountService
						.getAccountById(senderAccountNumber);
				SavingsAccount receiverSavingsAccount = savingsAccountService
						.getAccountById(receiverAccountNumber);
				savingsAccountService.fundTransfer(senderSavingsAccount,
						receiverSavingsAccount, amount);
				// response.sendRedirect("getAllForm.mm");
			} catch (ClassNotFoundException | SQLException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
			break;

		case "/searchForm.mm":
			response.sendRedirect("SearchForm.jsp");
			break;

		case "/searchByAccountNumber.mm":
			response.sendRedirect("searchByAccountNumber.jsp");
			break;
			
		case "/searchByAccNumber.mm": {
			int accountNumber = Integer.parseInt(request
					.getParameter("txtAccountNumber"));
			try {
				System.out.println("Intry");
				SavingsAccount account = savingsAccountService
						.getAccountById(accountNumber);
				request.setAttribute("account", account);
				dispatcher = request.getRequestDispatcher("AccountDetails.jsp");
				dispatcher.forward(request, response);
			} catch (ClassNotFoundException | SQLException
					| AccountNotFoundException e) {
				e.printStackTrace();
			}
		}
			break;
		case "/searchByAccountHolderName.mm" :
			response.sendRedirect("searchByAccountHolderName.jsp");
			break;
			
		case "/searchByHolderName.mm": {
			String accountHolderName = request.getParameter("txtHolderNumber");
			try {
				SavingsAccount account = savingsAccountService
						.getAccountByName(accountHolderName);
				request.setAttribute("account", account);
				dispatcher = request.getRequestDispatcher("AccountDetails.jsp");
				dispatcher.forward(request, response);
			} catch (ClassNotFoundException | SQLException
					| AccountNotFoundException e) {
				e.printStackTrace();
			}
		}
			break;
		case "/getAllForm.mm":
			try {
				List<SavingsAccount> accounts = savingsAccountService
						.getAllSavingsAccount();
				request.setAttribute("accounts", accounts);
				dispatcher = request.getRequestDispatcher("AccountDetails.jsp");
				dispatcher.forward(request, response);
			} catch (ClassNotFoundException | SQLException e) {
				e.printStackTrace();
			}
			break;
		case "/sortByName.mm":
			sort = !sort;
			result = sort ? 1 : -1;

			try {
				List<SavingsAccount> accountList = new ArrayList<SavingsAccount>();
				accountList = savingsAccountService.getAllSavingsAccount();
				Collections.sort(accountList, new Comparator<SavingsAccount>() {
					@Override
					public int compare(SavingsAccount arg0, SavingsAccount arg1) {
						return result
								* arg0.getBankAccount()
										.getAccountHolderName()
										.compareTo(
												arg1.getBankAccount()
														.getAccountHolderName());
					}
				});
				request.setAttribute("accounts", accountList);
				dispatcher = request.getRequestDispatcher("AccountDetails.jsp");
				dispatcher.forward(request, response);

			} catch (ClassNotFoundException | SQLException e1) {
				e1.printStackTrace();
			}

			break;

		case "/sortByBalance.mm": {
			sort = !sort;
			result = sort ? 1 : -1;

			try {
				List<SavingsAccount> accountList = new ArrayList<SavingsAccount>();
				accountList = savingsAccountService.getAllSavingsAccount();
				Collections.sort(accountList, new Comparator<SavingsAccount>() {
					@Override
					public int compare(SavingsAccount objectOne,
							SavingsAccount objectTwo) {
						return (int) (result * (objectOne.getBankAccount()
								.getAccountBalance() - (objectTwo
								.getBankAccount().getAccountBalance())));
					}
				});
				request.setAttribute("accounts", accountList);
				dispatcher = request.getRequestDispatcher("AccountDetails.jsp");
				dispatcher.forward(request, response);

			} catch (ClassNotFoundException | SQLException e1) {
				e1.printStackTrace();
			}

		}
			break;
		case "/sortBySalary.mm": {
			sort = !sort;
			result = sort ? 1 : -1;

			try {
				List<SavingsAccount> accountList = new ArrayList<SavingsAccount>();
				accountList = savingsAccountService.getAllSavingsAccount();
				Collections.sort(accountList, new Comparator<SavingsAccount>() {
					@Override
					public int compare(SavingsAccount objectOne,
							SavingsAccount objectTwo) {
						if (objectOne.isSalary() == true)
							return 1 * result;
						else
							return -1 * result;
					}
				});
				request.setAttribute("accounts", accountList);
				dispatcher = request.getRequestDispatcher("AccountDetails.jsp");
				dispatcher.forward(request, response);

			} catch (ClassNotFoundException | SQLException e1) {
				e1.printStackTrace();
			}

		}
			break;

		case "/updateAccount.mm":
			response.sendRedirect("updateForm.jsp");
			break;

		case "/update.mm": {
			int accountNumber1 = Integer.parseInt(request
					.getParameter("txtAccountNumber"));
			try {
				SavingsAccount account = savingsAccountService
						.getAccountById(accountNumber1);
				request.setAttribute("account", account);
				dispatcher = request.getRequestDispatcher("AccountDetails.jsp");
				dispatcher.forward(request, response);
			} catch (ClassNotFoundException | SQLException
					| AccountNotFoundException e) {
				e.printStackTrace();
			}
		}
			break;
		default:
			break;
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
