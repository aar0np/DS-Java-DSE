package com.datastax.products;

import com.datastax.cassandraconn.AstraConnection;
import com.datastax.products.ProductDAL.Promotion;
import com.datastax.products.ProductDAL.Product;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RequestMapping("/productsvc")
@RestController
public class ProductController {

	private ProductDAL productDAL;
	
	public ProductController() {
		AstraConnection conn = new AstraConnection();
		productDAL = new ProductDAL(conn.getCqlSession());
	}
	
	@GetMapping("/products/{productid}")
	public ResponseEntity<Product> getProduct(HttpServletRequest req,
            @PathVariable(value = "productid") 
            String productId) {
		
		Optional<Product> product = productDAL.getProduct(productId);
		
		if (product.isPresent()) {
			return ResponseEntity.ok(product.get());
		}
		
		return ResponseEntity.notFound().build();
	}
	
	@GetMapping("/promoproducts/{productid}")
	public ResponseEntity<Promotion> getPromotionProduct(HttpServletRequest req,
            @PathVariable(value = "productid") 
            String productId) {

		// get original product detail
		Optional<Promotion> promoProd = productDAL.getPromoProdByID(productId);
				
		if (promoProd.isPresent()) {
			// product exists, now query by its vector to get the closest product match
			Promotion originalProduct = promoProd.get();
			Optional<Promotion> annProd = productDAL.getPromoProdByVector(originalProduct);

			return ResponseEntity.ok(annProd.get());
		}
		
		return ResponseEntity.notFound().build();
	}
}
