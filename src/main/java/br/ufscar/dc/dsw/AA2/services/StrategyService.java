package br.ufscar.dc.dsw.AA2.services;

import br.ufscar.dc.dsw.AA2.dtos.StrategyDTO;
import br.ufscar.dc.dsw.AA2.models.Image;
import br.ufscar.dc.dsw.AA2.models.Strategy;
import br.ufscar.dc.dsw.AA2.repositories.ImageRepository;
import br.ufscar.dc.dsw.AA2.repositories.StrategyRepository;
import br.ufscar.dc.dsw.AA2.storage.StorageProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@Service
public class StrategyService {
    @Autowired
    private StrategyRepository strategyRepository;

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private StorageProvider storageProvider;

    public List<Strategy> getAll() {
        return strategyRepository.findAll();
    }

    public void deleteById(UUID id) {
        strategyRepository.deleteById(id);
    }

    public void insert(StrategyDTO strategyDTO, List<MultipartFile> imagesFiles) {
        Strategy strategy = new Strategy();
        strategy.setName(strategyDTO.name());
        strategy.setDescription(strategyDTO.description());
        strategy.setTips(strategyDTO.tips());
        strategy.setExamples(strategyDTO.examples());

        strategyRepository.save(strategy);

        this.uploadImages(strategy, imagesFiles);
    }

    private void uploadImages(Strategy strategy, List<MultipartFile> imagesFiles) {
        imagesFiles.forEach(imageFile -> {
            storageProvider.validateFile(imageFile);
            String imageUrl = storageProvider.store(imageFile);

            Image image = new Image();
            image.setUrl(imageUrl);
            image.setStrategy(strategy);
            imageRepository.save(image);
        });
    }
}
