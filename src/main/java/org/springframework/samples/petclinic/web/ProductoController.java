package org.springframework.samples.petclinic.web;

import java.text.ParseException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.LineaPedido;
import org.springframework.samples.petclinic.model.Pedido;
import org.springframework.samples.petclinic.model.Producto;
import org.springframework.samples.petclinic.model.ProductoDTO;
import org.springframework.samples.petclinic.model.TipoProducto;
import org.springframework.samples.petclinic.service.LineaPedidoService;
import org.springframework.samples.petclinic.service.PedidoService;
import org.springframework.samples.petclinic.service.ProductoService;
import org.springframework.samples.petclinic.service.ProveedorService;
import org.springframework.samples.petclinic.service.TipoProductoService;
import org.springframework.samples.petclinic.service.exceptions.DuplicatedPedidoException;
import org.springframework.samples.petclinic.service.exceptions.PedidoPendienteException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.extern.slf4j.Slf4j;
@Slf4j
@Controller
@RequestMapping(value = "/producto")
public class ProductoController {
	@Autowired
	private ProductoService productoService;
	@Autowired 
	private ProveedorService proveedorService;
	@Autowired
	private LineaPedidoService lineaPedidoService;
	@Autowired
	private TipoProductoService tipoProductoService;
	@Autowired
	private PedidoService pedidoService;
	@Autowired
	private ProductoConverter productoConverter;
	@Autowired
	private TipoProductoFormatter tipoProductoFormatter;
	@Autowired
	private ProveedorFormatter proveedorFormatter;
	
	public ProductoController(ProductoService productoService, ProveedorService proveedorService,
			LineaPedidoService lineaPedidoService, TipoProductoService tipoProductoService, PedidoService pedidoService,
			ProductoConverter productoConverter, TipoProductoFormatter tipoProductoFormatter,
			ProveedorFormatter proveedorFormatter) {
		super();
		this.productoService = productoService;
		this.proveedorService = proveedorService;
		this.lineaPedidoService = lineaPedidoService;
		this.tipoProductoService = tipoProductoService;
		this.pedidoService = pedidoService;
		this.productoConverter = productoConverter;
		this.tipoProductoFormatter = tipoProductoFormatter;
		this.proveedorFormatter = proveedorFormatter;
	}

	@ModelAttribute("tipoproducto") 				//Esto pertenece a TipoProducto
	public Collection<TipoProducto> poblarTiposProducto() {
		return this.tipoProductoService.findAll();
	}
	
	@GetMapping()
	public String listadoProducto(ModelMap modelMap) {
		String vista= "producto/listaProducto";
		Iterable<Producto> producto = productoService.findAll();
		Iterator<Producto> it_producto = producto.iterator();
		
		if (!(it_producto.hasNext())) {
			modelMap.addAttribute("message", "No hay productos, los necesitas para poder cocinar, añade uno nuevo");
		}
		modelMap.addAttribute("producto",producto);
		return vista;	
	}
	
	@GetMapping(path="/notificaciones")
	public String productos_que_faltan(ModelMap modelMap) {
		String vista= "producto/notificaciones";
		Collection<Producto> lista = new ArrayList<Producto>();
		Iterable<Producto> producto = productoService.findAll();
		Iterator<Producto> iter = producto.iterator();
		while(iter.hasNext()) {
			Producto product = iter.next();
			if(product.getCantMin() > product.getCantAct()) {
				lista.add(product);
			}
		}
		Iterable<Producto> res = lista;
		modelMap.addAttribute("producto",res);
		return vista;	
	}
	
	@GetMapping(path="/new")
	public String crearProducto(ModelMap modelMap) {
		String vista= "producto/editProducto";
		Collection<TipoProducto> collectionTipoProducto = this.tipoProductoService.findAll();
		List<String> collectionProveedor = this.proveedorService.findActivosName();
		modelMap.addAttribute("producto",new ProductoDTO());
		modelMap.addAttribute("listaProveedores", collectionProveedor);
		modelMap.addAttribute("listaTipos", collectionTipoProducto);
		return vista;
	}
		
	@PostMapping(path="/save")
	public String guardarProducto(ProductoDTO producto,BindingResult result,ModelMap modelMap) throws ParseException {
		String vista= "producto/listaProducto";
		final Producto productoFinal = productoConverter.convertProductoDTOToEntity(producto);
		productoFinal.setTipoProducto(tipoProductoFormatter.parse(producto.getTipoproductodto(), Locale.ENGLISH));
		productoFinal.setProveedor(proveedorFormatter.parse(producto.getProveedor(), Locale.ENGLISH));
			
		if(result.hasErrors()) {
			log.info(String.format("Product with name %s wasn't able to be created", producto.getName()));
			modelMap.addAttribute("producto", producto);
			return "producto/editProducto";
		}else {
			productoService.save(productoFinal);
			modelMap.addAttribute("message", "Guardado Correctamente");
			vista=listadoProducto(modelMap);
		}
		return vista; 
	}
		
	@GetMapping(path="/delete/{productoId}")
	public String borrarProducto(@PathVariable("productoId") int productoId, ModelMap modelMap)  {
		String vista= "producto/listaProducto";
		Optional<Producto> prod= productoService.findById(productoId);
		if(prod.isPresent()) {
			try {
				productoService.deleteById(productoId);
				modelMap.addAttribute("message", "Borrado Correctamente");
				vista=listadoProducto(modelMap);
			}catch (PedidoPendienteException ex) {
				modelMap.addAttribute("message", "No se puede borrar porque hay un pedido pendiente con ese producto");
				vista=listadoProducto(modelMap);
			}
		}
		else {
			modelMap.addAttribute("message", "Producto no encontrado");
			vista=listadoProducto(modelMap);
		}
		return vista;
	}

	@GetMapping(value = "/edit/{productoId}")
	public String initUpdateProductoForm(@PathVariable("productoId") int productoId, ModelMap model) {		
		String vista= "producto/editarProducto";	
		Collection<TipoProducto> collectionTipoProducto = this.tipoProductoService.findAll();
		Producto producto =  productoService.findById(productoId).get();
		ProductoDTO productoConvertido = productoConverter.convertEntityToProductoDTO(producto);
		Collection<String> collectionProveedor = this.proveedorService.findAllNames();
		productoConvertido.setTipoproductodto(producto.getTipoProducto().getName());
		model.addAttribute("listaTipos", collectionTipoProducto);
		model.addAttribute("listaProveedores", collectionProveedor);
		model.addAttribute("producto", productoConvertido);
		return vista;
		}
	
	@PostMapping(value = "/edit")
	public String processUpdateProductoForm(ProductoDTO producto, BindingResult result,ModelMap modelMap) throws ParseException {
		final Producto productoFinal = productoConverter.convertProductoDTOToEntity(producto);
		productoFinal.setTipoProducto(tipoProductoFormatter.parse(producto.getTipoproductodto(), Locale.ENGLISH));
		productoFinal.setProveedor(proveedorFormatter.parse(producto.getProveedor(), Locale.ENGLISH));
		if(result.hasErrors()) {
			modelMap.addAttribute("producto", producto);
			return "producto/editarProducto";
		}else {
			this.productoService.save(productoFinal);
			modelMap.addAttribute("message", "Guardado Correctamente");
			return "redirect:/producto";
		}
	}		
		

	@GetMapping(path="/savePedido/{productoId}")
	public String recargarStock(@PathVariable("productoId") int productoId, ModelMap modelMap) {
		String vista= "producto/listaProducto";
		Optional<Producto> prodOpt= productoService.findById(productoId);
		if(prodOpt.isPresent()) {
			try {
			Producto producto = prodOpt.get();
			Collection<Producto> listaProducto = productoService.findByProveedor(producto);
			Pedido pedido = new Pedido();
			pedido.setProveedor(producto.getProveedor());
			pedido.setFechaPedido(LocalDate.now());
			pedido.setHaLlegado(Boolean.FALSE);
			pedidoService.save(pedido);
			LineaPedido lineaPedido = new LineaPedido();
			for(Producto p : listaProducto) {
				lineaPedido = lineaPedidoService.anadirLineaPedido(p, pedido);
				lineaPedidoService.save(lineaPedido);
			}
			modelMap.addAttribute("message", "Se ha creado el pedido correctamente");
			vista = listadoProducto(modelMap);
			}catch(DuplicatedPedidoException ex){
				modelMap.addAttribute("message", "Ya hay un pedido pendiente a ese proveedor ");
				vista=listadoProducto(modelMap);
			}
		}else {
			modelMap.addAttribute("message", "Producto no encontrado");
			vista=listadoProducto(modelMap);
		}
		return vista;
	}
}