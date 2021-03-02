package com.mamdy.soa.impl;

import com.mailjet.client.errors.MailjetSocketTimeoutException;
import com.mamdy.dao.CategoryRepository;
import com.mamdy.dao.ProductRepository;
import com.mamdy.entites.Category;
import com.mamdy.entites.Product;
import com.mamdy.enums.ProductStatusEnum;
import com.mamdy.enums.ResultEnum;
import com.mamdy.exception.MyException;
import com.mamdy.soa.CategoryService;
import com.mamdy.soa.ProductService;
import com.mamdy.utils.FileUploadUtility;
import com.mamdy.utils.MailJetUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collections;

@Service
@Transactional
public class ProductServiceImpl implements ProductService {

	@Value("${mailjet.key.public}")
	String mailjetPublicKey;

	@Value("${mailjet.key.secret}")
	String mailjetSecretKey;
	@Autowired
	private ProductRepository productRepository;
	@Autowired
	private CategoryRepository categoryRepository;

	@Autowired
	private CategoryService categoryService;

	@Override
	public Product saveProductInServerAndDataBase(String name, String marque, String description, double price, int quantity,
												  String fileName, Category category) throws IOException {
		Product p = new Product();
		p.setName(name);
		p.setBrand(marque);
		p.setDescription(description);
		p.setPrice(price);
		p.setQuantity(quantity);
		//p.setFile(file);
		//p.setPhoto(file.getBytes());
		p.setFileName(fileName);
		p.setPhotoUrl(Collections.singletonList(fileName));
		p.setCategory(category);
		p.setActive(true);
		p.setSupplierId(1);
		productRepository.save(p);
		// mise à jour liste des produits du category passé en parametre
		if (category != null) {
			category.getProducts().add(p);
			category.setActive(true);
			categoryRepository.save(category);
		}
		return p;

	}

	@Override
	public Product saveProduct(Product p) throws IOException {

		p = productRepository.save(p);
		// mise à jour liste des produits du category passé en parametre
		Category category = p.getCategory();
		if (category!= null) {
			category.getProducts().add(p);
			category.setActive(true);
			categoryRepository.save(category);
		}
		return p;
	}

	@Override
	public Product saveProductInServerAndDataBase(String name, String marque, String description, double price,
			int quantity, MultipartFile file, String fileName, Category category) throws IOException {
		Product p = new Product();
		p.setName(name);
		p.setBrand(marque);
		p.setDescription(description);
		p.setPrice(price);
		p.setQuantity(quantity);
		p.setFile(file);
		p.setPhoto(FileUploadUtility.compressBytes(file.getBytes()));
		p.setFileName(fileName);
		p.setPhotoUrl(Collections.singletonList(fileName));
		p.setPhotos(null);
		p.setCategory(category);
		p.setActive(true);
		p.setSupplierId(1);
		productRepository.save(p);
		// mise à jour liste des produits du categori passé en parametre
		if (category != null) {
			category.getProducts().add(p);
			category.setActive(true);
			categoryRepository.save(category);
		}
		return p;
	}


	@Override
	public void increaseStock(String productId, int amount) {
		Product productInfo = findOne(productId);
		if (productInfo == null) throw new MyException(ResultEnum.PRODUCT_NOT_EXIST);

		int update = productInfo.getProductStock() + amount;
		productInfo.setProductStock(update);
		productRepository.save(productInfo);

	}

	@Override
	public Product findOne(String productId) {
		Product product = productRepository.findById(productId).get();
		return product;
	}

	@Override
	public Product findByCode(String code) {
		return productRepository.findByCode(code);
	}

	@Override
	public void decreaseStock(String productCode, int amount) throws MailjetSocketTimeoutException {
		Product product = findByCode(productCode);
		if (product == null) throw new MyException(ResultEnum.PRODUCT_NOT_EXIST);

		int update = product.getProductStock() - amount;
		if (update < 1)
			update = 0;
			//Notifier le vendeur de la baisse de stock
			//envois de mail pour notifier le client de sa commande
			String body = "Bonjour Alpha, le produit " + product.getName() + " a son stock insuffisant.";
			MailJetUtils.informSellerStokLowest(
					"balphamamoudou2013@gmail.com",
					"balphamamoudou2013@gmail.com",
					"Stock insuffisant!!",
					body,
					mailjetPublicKey,
					mailjetSecretKey
			);


		//throw new MyException(ResultEnum.PRODUCT_NOT_ENOUGH);

		product.setProductStock(update);
		productRepository.save(product);


	}

	@Override
	public Product offSale(String productId) {
		Product productInfo = findOne(productId);
		if (productInfo == null) throw new MyException(ResultEnum.PRODUCT_NOT_EXIST);

		if (productInfo.getProductStatus() == ProductStatusEnum.DOWN.getCode()) {
			throw new MyException(ResultEnum.PRODUCT_STATUS_ERROR);
		}

		productInfo.setProductStatus(ProductStatusEnum.DOWN.getCode());
		return productRepository.save(productInfo);
	}

	@Override
	public Product onSale(String productId) {
		Product productInfo = findOne(productId);
		if (productInfo == null) throw new MyException(ResultEnum.PRODUCT_NOT_EXIST);

		if (productInfo.getProductStatus() == ProductStatusEnum.UP.getCode()) {
			throw new MyException(ResultEnum.PRODUCT_STATUS_ERROR);
		}


		productInfo.setProductStatus(ProductStatusEnum.UP.getCode());
		return productRepository.save(productInfo);
	}

	@Override
	public Product update(Product productInfo) {
		// if null throw exception
		categoryService.findByCategoryType(productInfo);
		if (productInfo.getProductStatus() > 1) {
			throw new MyException(ResultEnum.PRODUCT_STATUS_ERROR);
		}

		return productRepository.save(productInfo);
	}

	@Override
	public Product save(Product productInfo) {
		return update(productInfo);
	}

	@Override
	public void delete(String productId) {
		Product productInfo = findOne(productId);
		if (productInfo == null) throw new MyException(ResultEnum.PRODUCT_NOT_EXIST);
		productRepository.delete(productInfo);

	}

	@Override
	public Page<Product> findAll(Pageable pageable) {
		return productRepository.findAllByOrderById(pageable);
	}


}
