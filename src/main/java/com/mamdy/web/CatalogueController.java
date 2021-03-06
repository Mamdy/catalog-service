package com.mamdy.web;

import com.mailjet.client.errors.MailjetSocketTimeoutException;
import com.mamdy.dao.CategoryRepository;
import com.mamdy.dao.ClientRepository;
import com.mamdy.dao.PhotoRepository;
import com.mamdy.dao.ProductRepository;
import com.mamdy.dto.CategoryNameDto;
import com.mamdy.dto.ProductEssentialDataDto;
import com.mamdy.entites.Category;
import com.mamdy.entites.Client;
import com.mamdy.entites.Photo;
import com.mamdy.entites.Product;
import com.mamdy.soa.ProductService;
import com.mamdy.utils.FileUploadUtility;
import com.mamdy.utils.MailJetUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.utility.RandomString;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Controller
@RestController
@RequestMapping("")
@CrossOrigin("*")
@Slf4j
public class CatalogueController {

	@Value("${mailjet.key.public}")
	String mailjetPublicKey;

	@Value("${mailjet.key.secret}")
	String mailjetSecretKey;

	private CategoryRepository categoryRepository;
	private ProductRepository productRepository;
	private PhotoRepository photoRepository;
	private ClientRepository clientRepository;

	Integer i = 0;

	public CatalogueController(ProductService productService, CategoryRepository categoryRepository, ProductRepository productRepository, PhotoRepository photoRepository, ClientRepository clientRepository) {
		this.categoryRepository = categoryRepository;
		this.productRepository = productRepository;
		this.photoRepository = photoRepository;
		this.clientRepository = clientRepository;
	}

	@PostMapping("/upload/saveProductInserverAndDataBaseWithFileUploadUtility")
	public ResponseEntity<Photo> uploaImage(
			@Valid @RequestParam("imageFile") final MultipartFile file
	) throws IOException {

		log.info("Original Image Byte Size -" + file.getBytes().length);
		Photo photo = new Photo();
		photo.setName(file.getOriginalFilename());
		photo.setType(file.getContentType());
		photo.setImg(FileUploadUtility.compressBytes(file.getBytes()));
		photo = photoRepository.save(photo);
		return ResponseEntity.ok(photo);

	}

	@GetMapping(path = {"/image/{imageName}"})
	public Photo getProductPhoto(@PathVariable("imageName") final String imageName) {
		Photo photoRetrieve = photoRepository.findByName(imageName);
		Photo newPhoto = new Photo();
		if (photoRetrieve != null) {
			newPhoto.setName(photoRetrieve.getName());
			newPhoto.setType(photoRetrieve.getType());
			newPhoto.setImg(FileUploadUtility.decompressBytes(photoRetrieve.getImg()));

		}
		return newPhoto;
	}

	@GetMapping("/photos")
	public Collection<Photo> getPhotos() {
		Collection<Photo> photos = photoRepository.findAll();
		photos.forEach(photo -> {
			photo.setImg(FileUploadUtility.decompressBytes(photo.getImg()));
		});
		return photos;
	}

	@GetMapping(path = {"/photos/{id}"})
	public Collection<Photo> getProductPhotos(@PathVariable("id") String id) {
		Collection<Photo> photos = null;
		Product retrievedProduct = productRepository.findById(id).get();
		if (retrievedProduct != null) {
			photos = retrievedProduct.getPhotos();
			photos.forEach(photo -> {
				photo.setImg(FileUploadUtility.decompressBytes(photo.getImg()));

			});
		}
		return photos;
	}
	@PatchMapping(value = "/categoryName/{id}")
	public boolean editCategoryName(
			@RequestBody CategoryNameDto categoryNameDto,
			@PathVariable("id") final String id ){
		this.categoryRepository.findById(id)
				.map(category -> {
					category.setName(categoryNameDto.getName());

					this.categoryRepository.save(category);
					return Boolean.TRUE;
				})
				.orElseGet(()->{
					Category newCategory = new Category();
					newCategory.setName(categoryNameDto.getName());
					categoryRepository.save((newCategory));
					return Boolean.TRUE;
				});

		return true;
	}

	@GetMapping("/allProducts")
	public List<Product> getAllProducts(){
		List<Product> productList = this.productRepository.findAll();
		List<Product> finalProducts = new ArrayList<>();

		for(Product product: productList){
			List<Photo> photos = product.getPhotos();
			photos.forEach(photo->{
				photo.setImg(FileUploadUtility.decompressBytes(photo.getImg()));
			});
			product.setPhotos(photos);
			finalProducts.add(product);
		}

		//System.out.println("photo decompresser " +finalProducts.get(20).getPhotos().get(0).getImg());
		return finalProducts;

	}

	/*@GetMapping("/products")
	public Page<Product> getAllProducts(Pageable pageable){
		Pageable wholePage = Pageable.unpaged();
		return this.productRepository.findAll(wholePage);
	}*/


	@PatchMapping(value = "/updateProduct/{id}")
	public Product updateProduct(@RequestBody ProductEssentialDataDto patchProduct,
												  @PathVariable("id") final String id){
		AtomicReference<Product> updatedProduct= new AtomicReference<Product>();
		this.productRepository.findById(id)
				.map(product->{
					product.setName(patchProduct.getName());
					product.setBrand(patchProduct.getBrand());
					product.setDescription(patchProduct.getDescription());
					product.setPrice(patchProduct.getPrice());
					product.setProductStock(patchProduct.getStock());
					product = this.productRepository.save(product);
					updatedProduct.set(product);

					return updatedProduct;
				})
				.orElseGet(()->{
					Product newProduct = new Product();
					newProduct.setName(patchProduct.getName());
					newProduct.setBrand(patchProduct.getBrand());
					newProduct.setDescription(patchProduct.getDescription());
					newProduct.setPrice(patchProduct.getPrice());
					newProduct.setProductStock(patchProduct.getStock());
					newProduct = this.productRepository.save(newProduct);
					updatedProduct.set(newProduct);
					return updatedProduct;
				});

		return updatedProduct.get();
	}
	// Test avec ma classe Utilitaire
	@PostMapping("/saveProduct")
	public Product saveProduct(@RequestBody final ProductFormData productFormData) throws IOException {
		//on map notre objet de la requête
		//ProductFormData productFormData = new ObjectMapper().readValue(formProduct, ProductFormData.class);
		//on recupère la liste des photos du produit qu'on veut enregistrer
		List<String> imagesNames = productFormData.getFilesNames();
		List<Photo> productPhotos = new ArrayList<>();
		Photo photo = null;
		for (String imageName : imagesNames) {
			photo = photoRepository.findByName(imageName);
			if (photo != null) {
				productPhotos.add(photo);
			}

		}


		Category c = categoryRepository.findByName(productFormData.getRegisterFormData().getCategory());
		if (c != null) {	
			double price = productFormData.getRegisterFormData().getPrice();
			double currentPrice = price-(price * 0.3);

			Product p = new Product();
			p.setName(productFormData.getRegisterFormData().getName());
			p.setCode(RandomString.make(5) + System.currentTimeMillis());
			p.setBrand(productFormData.getRegisterFormData().getMarque());
			p.setDescription(productFormData.getRegisterFormData().getDescription());
			p.setPrice(productFormData.getRegisterFormData().getPrice());
			p.setCurrentPrice(currentPrice);
			p.setQuantity(productFormData.getRegisterFormData().getQuantity());
			p.setProductStock(productFormData.getRegisterFormData().getQuantity());
			p.setCategory(c);
			p.setAvailable(true);
			p.setActive(true);
			p.setSupplierId(1);
			p.setPhotos(productPhotos);
			p = productRepository.save(p);

			if (p != null) {
				//mise à jour de la liste des produits de la categorie c
				c.getProducts().add(p);
				categoryRepository.save(c);
				Product finalP = p;

				//changer les noms initiaux des photos par le prefixe nom du nouveau produit
				productPhotos.forEach(img -> {
					String extension = FilenameUtils.getExtension(img.getName());
					String fileName = finalP.getName() + (i) + "_" + System.currentTimeMillis() + "." + extension;
					img.setName(fileName);
					//creer le lien entre la photo et son produit
					img.setProduct(finalP);
					photoRepository.save(img);
					i++;
				});

				return finalP;
			}
		}

		return null;
	}



	// Test avec ma classe Utilitaire
//	@PostMapping("/saveProductInserverAndDataBaseWithFileUploadUtility")
//	public ResponseEntity<Response> saveProductInserverFileUploadUtiliy(
//			@Valid @RequestParam("file") final MultipartFile file,
//			@Valid @RequestParam("formProduct") final String formProduct,
//			HttpServletRequest request) throws JsonParseException, JsonMappingException, IOException {
//		ProductFormData productFormData = new ObjectMapper().readValue(formProduct, ProductFormData.class);
//		// new ProductFormValidator().validate(productFormData, null);
//		// verification des erreur
//
//
//		// enregistrement des images dans le serveur
//		String fileName = file.getOriginalFilename();
//		String modifiedFileName = FilenameUtils.getBaseName(fileName) + "_" + System.currentTimeMillis() + "."
//				+ FilenameUtils.getExtension(fileName);
//
//		log.info("Original Image Byte Size -" + file.getBytes().length);
//
//		//on enregistre d'abord la photo en base
//		Photo photo = new Photo();
//		photo.setName(file.getOriginalFilename());
//		photo.setType(file.getContentType());
//		photo.setImg(FileUploadUtility.compressBytes(file.getBytes()));
//		photoRepository.save(photo);
//
//		//puis on enregistre le produit en base
//		Category c = categoryRepository.findByName(productFormData.getCategory());
//		if (c != null) {
//			Product dbProduct = productService.saveProductInServerAndDataBase(productFormData.getName(),
//					productFormData.getMarque(), productFormData.getDescription(), productFormData.getPrice(),
//					productFormData.getQuantity(), file, modifiedFileName, c);
//			if (dbProduct != null) {
//				if (dbProduct.getFile()!=null) {
//					//uploader notre fichier dans le server
//					FileUploadUtility.uplaodFile(request, dbProduct.getFile(), dbProduct.getId());
//					return new ResponseEntity<Response>(new Response("Product is Saved Successfully"), HttpStatus.OK);
//				}
//			} else {
//				return new ResponseEntity<Response>(new Response("Product is Not Saved"), HttpStatus.BAD_REQUEST);
//			}
//		}
//
//		return null;
//
//	}


	@GetMapping(value = "/searchKeyWord")
	public Page<Product> searchProductByKeyWord(@RequestParam("keyword") String keyword,
												@RequestParam(value = "page", defaultValue = "1") Integer page,
												@RequestParam(value = "size", defaultValue = "10") Integer size) {
		PageRequest request = PageRequest.of(page - 1, size);
		Page<Product> productPage = productRepository.findByNameContaining(keyword, request);

		return productPage;
	}

	@GetMapping(value = "/searchByCategory")
	public Page<Product> searchProductByCategory(@RequestParam("categoryName") String categoryName,
												@RequestParam(value = "page", defaultValue = "1") Integer page,
												@RequestParam(value = "size", defaultValue = "10") Integer size) {
		PageRequest request = PageRequest.of(page - 1, size);
		Page<Product> productPage = null;

		Category category = categoryRepository.findByNameLike(categoryName.toLowerCase());
		if(category!=null){
			productPage = productRepository.findByCategory(category, request);
		}
		

		return productPage;
	}

	@GetMapping(value = "/searchByCode/{code}")
	public Product searchProductByCategory(@PathVariable("code") final String code) {
		Product product = productRepository.findByCode(code);
		return product;
	}

	@GetMapping(path = {"/resetPassword/{email}"})
	public boolean sendResetPasswordLink(@PathVariable("email") final String email) throws MailjetSocketTimeoutException {
		Client client = this.clientRepository.findByEmail(email);
		if (client != null) {
			MailJetUtils.sendResetPasswordLink(
					"balphamamoudou2013@gmail.com",
					client.getEmail(),
					"No reply GN-LOUMOSTORE: Réinitialisation de votre mot de passe",
					" Bonjour " + client.getFirstName() + ", \n",
					mailjetPublicKey,
					mailjetSecretKey

			);
			return true;
		} else throw new RuntimeException("Cette adresse e-mail n'existe pas dans notre base de données");
	}
}


@Data
class ProductFormData {
	/*private String name;
	private String marque;
	private String description;
	private double price;
	private int quantity;
	//private MultipartFile file;
//	private byte[] photo;
//	private String fileName;
	private String category;*/
	private RegisterFormData registerFormData;
	private List<String> filesNames = new ArrayList<>();

}

@Data
class RegisterFormData {
	private String name;
	private String marque;
	private String description;
	private double price;
	private int quantity;
	private String category;

}
