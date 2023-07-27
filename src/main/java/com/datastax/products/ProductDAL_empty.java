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

	}
	
	public Optional<Product> getProduct(String productId) {

	}
	
	public Optional<Promotion> getPromoProdByID(String productId) {

	}
	
	public Optional<Promotion> getPromoProdByVector(Promotion originalProduct) {

	}
}
