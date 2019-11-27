package kh.jomalone.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;

import org.apache.tomcat.dbcp.dbcp2.BasicDataSource;

import kh.jomalone.DTO.BuyDTO;
import kh.jomalone.DTO.OrderListDTO;

public class BuyDAO {
	private static BuyDAO instance;
	private BasicDataSource bds = new BasicDataSource();

	private BuyDAO() {
		bds.setDriverClassName("oracle.jdbc.driver.OracleDriver");
		bds.setUrl("jdbc:oracle:thin:@localhost:1521:xe");
		bds.setUsername("jomalone");
		bds.setPassword("jomalone");
		bds.setInitialSize(30);
	}

	public synchronized static BuyDAO getInstance() {
		if (instance == null) {
			instance = new BuyDAO();
		}
		return instance;
	}

	public Connection getConnection() throws Exception {
		return bds.getConnection();
	}

	public int insertBuyProduct(BuyDTO dto) throws Exception {
		try (Connection con = this.getConnection();
				PreparedStatement pstat = con.prepareStatement(
						"insert into prod_buy values(buy_seq.nextval,sysdate,?,?,?,?,?,?,?,?,?,?,?)");) {
			pstat.setString(1, dto.getPg());
			pstat.setString(2, dto.getPay_method());
			pstat.setString(3, dto.getMerchant_uid());
			pstat.setString(5, dto.getName());
			pstat.setInt(6, dto.getTotalPrice());
			pstat.setString(7, dto.getMem_id());
			pstat.setString(8, dto.getMem_name());
			pstat.setString(9, dto.getMem_phone());
			pstat.setString(10, dto.getMem_email());
			pstat.setString(11, dto.getFull_address());
			pstat.setString(12, dto.getZip_code());
			int result = pstat.executeUpdate();
			con.commit();
			return result;
		}
	}

	public void insertOrderList(List<OrderListDTO> list) throws Exception {
		try (Connection con = this.getConnection();
				PreparedStatement pstat = con.prepareStatement(
						"insert into prod_buy values(order_seq.nextval,sysdate,?,?,?,?,?,?,'주문 완료',?,?,?,?,'배송 준비중',N)");) {
			for (OrderListDTO dto : list) {
				pstat.setString(1, dto.getMerchant_uid());
				pstat.setString(2, dto.getProd_code());
				pstat.setString(3, dto.getProd_name());
				pstat.setString(4, dto.getPay_method());
				pstat.setString(5, dto.getMem_id());
				pstat.setString(6, dto.getMem_name());
				pstat.setInt(7, dto.getProd_quantity());
				pstat.setInt(8, dto.getPrice());
				pstat.setString(9, dto.getFull_address());
				pstat.setString(10, dto.getZip_code());
				pstat.executeUpdate();
				con.commit();
			}
		}
	}

}
