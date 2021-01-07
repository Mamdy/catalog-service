package com.mamdy.web;

import com.mamdy.dao.CategoryRepository;
import com.mamdy.dao.PhotoRepository;
import com.mamdy.dao.ProductRepository;
import com.mamdy.entites.Category;
import com.mamdy.entites.Photo;
import com.mamdy.entites.Product;
import com.mamdy.soa.ProductService;
import com.mamdy.utils.FileUploadUtility;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletContext;
import javax.validation.Valid;
import java.io.IOException;
import java.util.*;

@Controller
@RestController
@RequestMapping("")
@Slf4j
public class CatalogueController {
	private CategoryRepository categoryRepository;
	private ProductRepository productRepository;
	private PhotoRepository photoRepository;

	Set<Photo> productPhotos = new HashSet<>();

	Integer i = 0;

	public CatalogueController(ProductService productService, CategoryRepository categoryRepository, ProductRepository productRepository, PhotoRepository photoRepository) {
		this.categoryRepository = categoryRepository;
		this.productRepository = productRepository;
		this.photoRepository = photoRepository;
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

	@GetMapping(path = { "/image/{imageName}" })
	public Photo getProductPhoto(@PathVariable("imageName") String imageName){
		Photo photoRetrieve = photoRepository.findByName(imageName);
		Photo newPhoto = new Photo();
		if(photoRetrieve!=null){
			newPhoto.setName(photoRetrieve.getName());
			newPhoto.setType(photoRetrieve.getType());
			newPhoto.setImg(FileUploadUtility.decompressBytes(photoRetrieve.getImg()));

		}
		return  newPhoto;
	}

	@GetMapping("/photos")
	public Collection<Photo> getPhotos(){
		Collection<Photo> photos = photoRepository.findAll();
		photos.forEach(photo -> {
			photo.setImg(FileUploadUtility.decompressBytes(photo.getImg()));
		});
		return photos;
	}

	@GetMapping(path = { "/photos/{id}" })
	public Collection<Photo> getProductPhotos(@PathVariable("id") String id){
		Collection<Photo> photos = null;
		Product retrievedProduct = productRepository.findById(id).get();
		if(retrievedProduct!=null){
			photos = retrievedProduct.getPhotos();
			photos.forEach(photo-> {
				photo.setImg(FileUploadUtility.decompressBytes(photo.getImg()));

			});
		}
		return  photos;
	}

	// Test avec ma classe Utilitaire
	@PostMapping("/saveProduct")
	public Product saveProduct(@RequestBody final ProductFormData productFormData) throws IOException {
		//on map notre objet de la requête
		//ProductFormData productFormData = new ObjectMapper().readValue(formProduct, ProductFormData.class);
		//on recupère la liste des photos du produits
		List<String> imagesNames = productFormData.getFilesNames();
		productPhotos.clear();
		for (String imageName : imagesNames) {
			productPhotos.add(photoRepository.findByName(imageName));
		}


		Category c = categoryRepository.findByName(productFormData.getRegisterFormData().getCategory());
		if (c != null) {
			Product p = new Product();
			p.setName(productFormData.getRegisterFormData().getName());
			p.setBrand(productFormData.getRegisterFormData().getMarque());
			p.setDescription(productFormData.getRegisterFormData().getDescription());
			p.setPrice(productFormData.getRegisterFormData().getPrice());
			p.setQuantity(productFormData.getRegisterFormData().getQuantity());
			p.setProductStock(productFormData.getRegisterFormData().getQuantity());
			p.setCategory(c);
			p.setAvailable(true);
			p.setActive(true);
			p.setSupplierId(1);
			p.setPhotos(productPhotos);
			p= productRepository.save(p);

			if (p != null) {
				//mise à jour de la liste des produits de la categorie c
			c.getProducts().add(p);
			categoryRepository.save(c);
				Product finalP = p;

				//changer les noms initiaux des photos par le prefixe nom du nouveau produit
				productPhotos.forEach(photo-> {
					String extension = FilenameUtils.getExtension(photo.getName());
					String fileName = finalP.getName() + (i) + "_" + System.currentTimeMillis()+ "." +extension;
					photo.setName(fileName);
					//creer le lien entre la photo et son produit
					photo.setProduct(finalP);
					photoRepository.save(photo);
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
