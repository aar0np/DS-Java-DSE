package com.datastax.products;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.BoundStatement;
import com.datastax.oss.driver.api.core.cql.PreparedStatement;
import com.datastax.oss.driver.api.core.cql.ResultSet;
import com.datastax.oss.driver.api.core.cql.Row;

public class ProductDAL {

	private CqlSession session;
	
	public record Product(String productId,
			String name, String productGroup,
			String brand, String shortDesc,
			Set<String> images) {
	}
	
	public record Promotion(String productId,
			String name,
			Object vector) {
	}

	public ProductDAL(CqlSession sess) {
		this.session = sess;
	}
	
	public Optional<Product> getProduct(String productId) {
		PreparedStatement qpPrepared = session.prepare("SELECT * FROM product WHERE product_id = ?");
		BoundStatement qpBound = qpPrepared.bind(productId);
		ResultSet rs = session.execute(qpBound);
		Row pRow = rs.one();
		
		if (pRow != null) {
			Product returnVal = new Product(productId, pRow.getString("name"),
				pRow.getString("product_group"), pRow.getString("brand"),
				pRow.getString("short_desc"), pRow.getSet("images", String.class));
			
			return Optional.of(returnVal);
		}
		
		return Optional.of(null);
	}
	
	public Optional<Promotion> getPromoProdByID(String productId) {
		
		PreparedStatement qpPrepared = session.prepare("SELECT * FROM product_vector WHERE product_id = ?");
		BoundStatement qpBound = qpPrepared.bind(productId);
		ResultSet rs = session.execute(qpBound);
		Row product = rs.one();
		
		if (product != null) {
			Promotion originalProduct = new Promotion(productId,
				product.getString("name"),
				product.getCqlVector("product_vector"));

			return Optional.of(originalProduct);
		} else {
			return Optional.of(null);
		}
	}
	
	public Optional<Promotion> getPromoProdByVector(Promotion originalProduct) {
		
		PreparedStatement qvPrepared = session.prepare("SELECT * FROM product_vector ORDER BY product_vector ANN OF ? LIMIT 2;");
		BoundStatement qvBound = qvPrepared.bind(originalProduct.vector());
		ResultSet rsV = session.execute(qvBound);
		List<Row> ann = rsV.all();
		
		if (ann.size() > 1) {
			// only add new product to promoProds list
			for (Row promo : ann) {
				String promoProdId = promo.getString("product_id");
				
				if (!promoProdId.equals(originalProduct.productId)) {
					Promotion annPromoProd = new Promotion(promoProdId,
							promo.getString("name"),
							promo.getCqlVector("product_vector"));
					//once we find it, no need to check the others
					return Optional.of(annPromoProd);
				}
			}
		}
		
		return Optional.of(null);
	}
}
