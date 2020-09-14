package com.mamdy.web;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mamdy.dao.CategoryRepository;
import com.mamdy.dao.ProductRepository;
import com.mamdy.entites.Category;
import com.mamdy.entites.Product;
import com.mamdy.soa.ProductService;
import com.mamdy.utils.FileUploadUtility;
import com.mamdy.utils.Response;
import lombok.Data;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.ValidationException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Controller
@RestController
@RequestMapping("")
public class CatalogueController {
	@Autowired
	private ProductService productService;
	@Autowired
	private CategoryRepository categoryRepository;
	
	@Autowired
	private ProductRepository productRepository;

	@Autowired
	ServletContext context;

	//@PostMapping("/saveProductInserverAndDataBaseWithFileUploadUtility")
	public Map<String, Set<String>> CreateProduct(@Valid @RequestBody ProductFormData productFormData, BindingResult result) {
		
 		if(result.hasErrors()) {
			throw new ValidationException("Le Formulaire contient une erreurs");
		}
		
		Map<String, Set<String>> errors = new HashMap<>();
//
//		for (FieldError fieldError : result.getFieldErrors()) {
//			String code = fieldError.getCode();
//			String field = fieldError.getField();
//			if (code.equals("NotBlank") || code.equals("NotNull")) {
//				errors.computeIfAbsent(field, key -> new HashSet<>()).add("required");
//			} else if (code.equals("Email") && field.equals("email")) {
//				errors.computeIfAbsent(field, key -> new HashSet<>()).add("pattern");
//			} else if (code.equals("Min") && field.equals("price")) {
//				errors.computeIfAbsent(field, key -> new HashSet<>()).add("le prix ne doit pas etre inferieur à 1");
//			} else if (code.equals("Min") && field.equals("quantity")) {
//				errors.computeIfAbsent(field, key -> new HashSet<>()).add("le prix ne doit pas etre inferieur à 1");
//			}
//
//			else if (code.equals("Size") && field.equals("name")) {
//				if (productFormData.getName().length() < 2) {
//					errors.computeIfAbsent(field, key -> new HashSet<>()).add("minlength");
//				} else {
//					errors.computeIfAbsent(field, key -> new HashSet<>()).add("maxlength");
//				}
//			} 
//			
////			else if (code.equals("Size") && field.equals("brand")) {
////				if (productFormData.getName().length() < 2) {
////					errors.computeIfAbsent(field, key -> new HashSet<>()).add("minlength");
////				} else {
////					errors.computeIfAbsent(field, key -> new HashSet<>()).add("maxlength");
////				}
////			}
//		}

		if (errors.isEmpty()) {
			System.out.println(productFormData);
		}

		// enregistrement des images dans le serveur
		/*String fileName = productFormData.getFile().getOriginalFilename();
		String modifiedFileName = FilenameUtils.getBaseName(fileName) + "_" + System.currentTimeMillis() + "."
				+ FilenameUtils.getExtension(fileName);*/

		Category c = categoryRepository.findByName(productFormData.getCategory());
		if (c != null) {
			try {
				Product dbProduct = productService.saveProductInServerAndDataBase(productFormData.getName(),
						productFormData.getMarque(), productFormData.getDescription(), productFormData.getPrice(),
						//productFormData.getQuantity(), productFormData.getFile(), modifiedFileName, c);
						productFormData.getQuantity(), null, c);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
//			if (dbProduct != null) {
//				if (!dbProduct.getFile().getOriginalFilename().equals("")) {
//
//					FileUploadUtility.uplaodFile(request, dbProduct.getFile(), dbProduct.getId());
//					return new ResponseEntity<Response>(new Response("Product is Saved Successfully"), HttpStatus.OK);
//				}
//			} else {
//				return new ResponseEntity<Response>(new Response("Product is Not Saved"), HttpStatus.BAD_REQUEST);
//			}
		}
		return errors;


	}

	// Test avec ma classe Utilitaire
	@PostMapping("/saveProduct")
	public ResponseEntity<Response> saveProduct(@RequestBody final String formProduct) throws IOException {
		ProductFormData productFormData = new ObjectMapper().readValue(formProduct, ProductFormData.class);

		Category c = categoryRepository.findByName(productFormData.getCategory());
		if (c != null) {
			Product dbProduct = productService.saveProduct(productFormData.getName(),
					productFormData.getMarque(), productFormData.getDescription(), productFormData.getPrice(),
					productFormData.getQuantity(), c);
			if (dbProduct != null) {
				return new ResponseEntity<Response>(new Response("Product is Saved Successfully"), HttpStatus.OK);
			}


		}

		return null;
	}


	// Test avec ma classe Utilitaire
	@PostMapping("/saveProductInserverAndDataBaseWithFileUploadUtility")
	public ResponseEntity<Response> saveProductInserverFileUploadUtiliy(
			@Valid @RequestParam("file") final MultipartFile file,
			@Valid @RequestParam("formProduct") final String formProduct,
			HttpServletRequest request) throws JsonParseException, JsonMappingException, IOException {
		ProductFormData productFormData = new ObjectMapper().readValue(formProduct, ProductFormData.class);
		// new ProductFormValidator().validate(productFormData, null);
		// verification des erreur
	

		// enregistrement des images dans le serveur
		String fileName = file.getOriginalFilename();
		String modifiedFileName = FilenameUtils.getBaseName(fileName) + "_" + System.currentTimeMillis() + "."
				+ FilenameUtils.getExtension(fileName);

		Category c = categoryRepository.findByName(productFormData.getCategory());
		if (c != null) {
			Product dbProduct = productService.saveProductInServerAndDataBase(productFormData.getName(),
					productFormData.getMarque(), productFormData.getDescription(), productFormData.getPrice(),
					productFormData.getQuantity(), file, modifiedFileName, c);
			if (dbProduct != null) {
				if (!dbProduct.getFile().getOriginalFilename().equals("")) {
					//uploader notre fichier dans le server
					FileUploadUtility.uplaodFile(request, dbProduct.getFile(), dbProduct.getId());
					return new ResponseEntity<Response>(new Response("Product is Saved Successfully"), HttpStatus.OK);
				}
			} else {
				return new ResponseEntity<Response>(new Response("Product is Not Saved"), HttpStatus.BAD_REQUEST);
			}
		}

		return null;

	}


	@GetMapping(value = "/searchKeyWord")
	public Page<Product> searchProductByKeyWord(@RequestParam("keyword") String keyword,
												@RequestParam(value = "page", defaultValue = "1") Integer page,
												@RequestParam(value = "size", defaultValue = "10") Integer size) {
		PageRequest request = PageRequest.of(page - 1, size);
		Page<Product> productPage = productRepository.findByNameContaining(keyword, request);

		return productPage;
	}


	/*@PostMapping("/saveProduct")
	public ResponseEntity<Response> saveProduct(@RequestParam("file") MultipartFile file,
			@RequestParam("formProduct") String formProduct)
			throws JsonParseException, JsonMappingException, IOException {
		ProductFormData productFormData = new ObjectMapper().readValue(formProduct, ProductFormData.class);
		Category c = categoryRepository.findByName(productFormData.getCategory());
		if (c != null) {
			Product dbProduct = productService.saveProduct(productFormData.getName(), productFormData.getMarque(),
					productFormData.getDescription(), productFormData.getPrice(), productFormData.getQuantity(), file,
					c);
			if (dbProduct != null) {
				return new ResponseEntity<Response>(new Response("Product is Saved Successfully"), HttpStatus.OK);
			} else {
				return new ResponseEntity<Response>(new Response("Product is Not Saved"), HttpStatus.BAD_REQUEST);
			}
		}

		return null;

	}*/

/*	@PostMapping("/saveProductInserverAndDataBase")
	public ResponseEntity<Response> saveProductInserver(@RequestParam("file") MultipartFile file,
			@RequestParam("formProduct") String formProduct)
			throws JsonParseException, JsonMappingException, IOException {
		ProductFormData productFormData = new ObjectMapper().readValue(formProduct, ProductFormData.class);
		boolean isExistProductDirectory = new File(context.getRealPath("/productfolder")).exists();
		if (!isExistProductDirectory) {
			new File(context.getRealPath("/productfolder")).mkdir();
		}

		// enregistrement des images dans le serveur
		String fileName = file.getOriginalFilename();
		String modifiedFileName = FilenameUtils.getBaseName(fileName) + "_" + System.currentTimeMillis() + "."
				+ FilenameUtils.getExtension(fileName);
		File serverFile = new File(context.getRealPath("productfolder/" + File.separator + modifiedFileName));
		try {
			FileUtils.writeByteArrayToFile(serverFile, file.getBytes());
		} catch (Exception e) {
			e.printStackTrace();
		}

		Category c = categoryRepository.findByName(productFormData.getCategory());
		if (c != null) {
			Product dbProduct = productService.saveProductInServerAndDataBase(productFormData.getName(),
					productFormData.getMarque(), productFormData.getDescription(), productFormData.getPrice(),
					productFormData.getQuantity(), file, modifiedFileName, c);
			if (dbProduct != null) {
				return new ResponseEntity<Response>(new Response("Product is Saved Successfully"), HttpStatus.OK);
			} else {
				return new ResponseEntity<Response>(new Response("Product is Not Saved"), HttpStatus.BAD_REQUEST);
			}
		}

		return null;

	}
*/


	// Test avec ma classe Utilitaire
//	@PostMapping("/saveProductInserverAndDataBaseWithFileUploadUtilitycsd")
//	public Map<String, Set<String>> saveProductInserverFileUploadUtiliye(
//			@Valid @RequestParam("file") final MultipartFile file,
//			@Valid @RequestParam("formProduct") final String formProduct, BindingResult results,
//			HttpServletRequest request, Model model) throws JsonParseException, JsonMappingException, IOException {
//		ProductFormData productFormData = new ObjectMapper().readValue(formProduct, ProductFormData.class);
//		// new ProductFormValidator().validate(productFormData, null);
//		// verification des erreur
////		if(results.hasErrors()) {
////			model.addAttribute("userCliqueMangementProduct", true);
////			model.addAttribute("title", "Manage Product");
////			return "Page";
////		}
//
//		Map<String, Set<String>> errors = new HashMap<>();
//
//		for (FieldError fieldError : results.getFieldErrors()) {
//			String code = fieldError.getCode();
//			String field = fieldError.getField();
//			if (code.equals("NotBlank") || code.equals("NotNull")) {
//				errors.computeIfAbsent(field, key -> new HashSet<>()).add("required");
//			} else if (code.equals("Email") && field.equals("email")) {
//				errors.computeIfAbsent(field, key -> new HashSet<>()).add("pattern");
//			} else if (code.equals("Min") && field.equals("price")) {
//				errors.computeIfAbsent(field, key -> new HashSet<>()).add("le prix ne doit pas etre inferieur à 1");
//			} else if (code.equals("Min") && field.equals("quantity")) {
//				errors.computeIfAbsent(field, key -> new HashSet<>()).add("le prix ne doit pas etre inferieur à 1");
//			}
//
//			else if (code.equals("Size") && field.equals("name")) {
//				if (productFormData.getName().length() < 2) {
//					errors.computeIfAbsent(field, key -> new HashSet<>()).add("minlength");
//				} else {
//					errors.computeIfAbsent(field, key -> new HashSet<>()).add("maxlength");
//				}
//			} else if (code.equals("Size") && field.equals("brand")) {
//				if (productFormData.getName().length() < 2) {
//					errors.computeIfAbsent(field, key -> new HashSet<>()).add("minlength");
//				} else {
//					errors.computeIfAbsent(field, key -> new HashSet<>()).add("maxlength");
//				}
//			}
//		}
//
//		if (errors.isEmpty()) {
//			System.out.println(productFormData);
//		}
//
//		// enregistrement des images dans le serveur
//		String fileName = file.getOriginalFilename();
//		String modifiedFileName = FilenameUtils.getBaseName(fileName) + "_" + System.currentTimeMillis() + "."
//				+ FilenameUtils.getExtension(fileName);
//
//		Category c = categoryRepository.findByName(productFormData.getCategory());
//		if (c != null) {
//			Product dbProduct = productService.saveProductInServerAndDataBase(productFormData.getName(),
//					productFormData.getMarque(), productFormData.getDescription(), productFormData.getPrice(),
//					productFormData.getQuantity(), file, modifiedFileName, c);
//			if (dbProduct != null) {
//				if (!dbProduct.getFile().getOriginalFilename().equals("")) {
//
//					FileUploadUtility.uplaodFile(request, dbProduct.getFile(), dbProduct.getId());
//					// return new ResponseEntity<Response>(new Response("Product is Saved
//					// Successfully"), HttpStatus.OK);
//				}
//			} else {
//				// return new ResponseEntity<Response>(new Response("Product is Not Saved"),
//				// HttpStatus.BAD_REQUEST);
//			}
//		}
//
//		return errors;
//
//	}

//	@PostMapping("/saveProductInServer")
//	public ResponseEntity<Response> saveProducter(@RequestBody ProductFormData productFormData)
//			throws JsonParseException, JsonMappingException, IOException {
//
//		Category c = categoryRepository.findByName(productFormData.getCategory());
//		if (c != null) {
//			Product dbProduct = productService.saveProduct(productFormData.getName(), productFormData.getMarque(),
//					productFormData.getDescription(), productFormData.getPrice(), productFormData.getQuantity(),
//					productFormData.getFile(), c);
//			if (dbProduct != null) {
//				return new ResponseEntity<Response>(new Response("Product is Saved Successfully"), HttpStatus.OK);
//			} else {
//				return new ResponseEntity<Response>(new Response("Product is Not Saved"), HttpStatus.BAD_REQUEST);
//			}
//		}
//
//		return null;
//
//	}

}

@Data
class ProductFormData {
	private String name;
	private String marque;
	private String description;
	private double price;
	private int quantity;
	//private MultipartFile file;
//	private byte[] photo;
//	private String fileName;
	private String category;

}
