package panda.web.beans;

import org.modelmapper.ModelMapper;
import panda.domain.models.binding.PackageCreateBindingModel;
import panda.domain.models.service.PackageServiceModel;
import panda.service.PackageService;
import panda.service.UserService;

import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Named
@RequestScoped
public class PackageCreateBean {

    private List<String> users;
    private PackageCreateBindingModel packageCreateBindingModel;
    private UserService userService;
    private PackageService packageService;
    private ModelMapper modelMapper;

    public PackageCreateBean() {
    }

    @Inject
    public PackageCreateBean(UserService userService, PackageService packageService, ModelMapper modelMapper) {
        this.userService = userService;
        this.packageService = packageService;
        this.modelMapper = modelMapper;
        this.initUsers();
        this.initModel();

    }

    private void initUsers() {
        this.users = this.userService
                .findAllUsers()
                .stream()
                .map(u -> u.getUsername())
                .collect(Collectors.toList());
    }

    private void initModel() {
        this.packageCreateBindingModel = new PackageCreateBindingModel();
    }

    public List<String> getUsers() {
        return users;
    }

    public void setUsers(List<String> users) {
        this.users = users;
    }

    public PackageCreateBindingModel getModel() {
        return packageCreateBindingModel;
    }

    public void setModel(PackageCreateBindingModel model) {
        this.packageCreateBindingModel = model;
    }

    public void createPackage() throws IOException {
        PackageServiceModel packageServiceModel = this.modelMapper
                .map(this.packageCreateBindingModel, PackageServiceModel.class);

        packageServiceModel
                .setRecipient(this.userService.findByUsername(this.packageCreateBindingModel.getRecipient()));

        this.packageService
                .packageCreate(packageServiceModel);

        FacesContext.getCurrentInstance()
                .getExternalContext()
                .redirect("/faces/view/home.xhtml");
    }
}
