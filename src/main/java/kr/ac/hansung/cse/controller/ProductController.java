package kr.ac.hansung.cse.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kr.ac.hansung.cse.model.Product;
import kr.ac.hansung.cse.repo.ProductRepository;

@RestController
@RequestMapping("/api/v1")
public class ProductController {
	@Autowired
	private ProductRepository productRepository;

	@PostMapping(value = "/products")
	public ResponseEntity<Product> createProduct(@RequestBody Product product) {
		try {
			Product p = productRepository.save(product);

			return new ResponseEntity<>(p, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping(value = "/products")
	public ResponseEntity<List<Product>> allProduct() {
		List<Product> products = new ArrayList<>();
		productRepository.findAll().forEach(products::add);
		if (products.isEmpty()) {
			return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<>(products, HttpStatus.OK);
	}

	@GetMapping(value = "/products/{id}")
	public ResponseEntity<Product> getProduct(@PathVariable("id") int id) {
		Optional<Product> product = productRepository.findById(id);
		if (product.isPresent()) {
			return new ResponseEntity<>(product.get(), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
		}
	}

	@GetMapping(value="/products/category/{category}")
	public ResponseEntity<List<Product>> categoryProducts(@PathVariable("category") String category){
		List<Product> products;
		try {
			products = productRepository.findByCategory(category);
			if(products.isEmpty()) {
				return new ResponseEntity<>(null,HttpStatus.NO_CONTENT);
			}
			return new ResponseEntity<>(products,HttpStatus.OK);
		}catch(Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PutMapping(value = "/products/{id}")
	public ResponseEntity<Product> getProduct(@PathVariable("id") int id, @RequestBody Product p) {
		Optional<Product> product = productRepository.findById(id);
		if (product.isPresent()) {
			Product newProduct = product.get();
			newProduct.setName(p.getName());
			newProduct.setCategory(p.getCategory());
			newProduct.setDescription(p.getDescription());
			newProduct.setManufacturer(p.getManufacturer());
			newProduct.setPrice(p.getPrice());
			newProduct.setUnitInStock(p.getUnitInStock());
			return new ResponseEntity<>(productRepository.save(newProduct), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
		}

	}

	@DeleteMapping(value = "/products/{id}")
	public ResponseEntity<Void> deleteProduct(@PathVariable("id") int id) {
		try {
			productRepository.deleteById(id);
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
	}
}
