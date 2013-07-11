package br.com.yaw.demojetty.web;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import br.com.yaw.demojetty.model.Veiculo;
import br.com.yaw.demojetty.repository.VeiculoRepository;

@RequestMapping("/veiculos")
@Controller
public class VeiculoController {

	@Autowired
	private VeiculoRepository dao;
	
	@RequestMapping(params = "form", produces = "text/html")
    public String createForm(Model uiModel) {
        return "veiculos/create";
    }
	
	@RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String create(@Valid Veiculo veiculo, BindingResult bindingResult,
    		Model uiModel) {
        if (bindingResult.hasErrors()) {
        	return "veiculos/create";
        }
        	
        uiModel.asMap().clear();
        dao.save(veiculo);
        return "redirect:/veiculos/" + veiculo.getId().toString();
    }
	
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
    public String delete(
    		@PathVariable("id") Long id, 
    		@RequestParam(value = "page", required = false) Integer page, 
    		@RequestParam(value = "size", required = false) Integer size, 
    		Model uiModel) {
        Veiculo veiculo = dao.findOne(id);
        //TODO validar null
        dao.delete(veiculo);
        uiModel.asMap().clear();
        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
        uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
        return "redirect:/veiculos";
    }
	
	@RequestMapping(value = "/{id}", produces = "text/html")
    public String show(@PathVariable("id") Long id, Model uiModel) {
        uiModel.addAttribute("veiculo", dao.findOne(id));
        return "veiculos/show";
    }
    
    @RequestMapping(produces = "text/html")
    public String list(
    		@RequestParam(value = "page", required = false) Integer page, 
    		@RequestParam(value = "size", required = false) Integer size,
    		Model uiModel) {
        int sizeNo = size == null ? 10 : size.intValue();
        if (page == null || page.intValue() < 1) {
			page = 1;
		}
        
        Pageable pageable = new PageRequest(page, sizeNo);
		Page<Veiculo> dados = dao.findAll(pageable);
        
        uiModel.addAttribute("veiculos", dados.getContent());
        float nrOfPages = (float) dados.getTotalPages();
        uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
    
        return "veiculos/list";
    }

}
