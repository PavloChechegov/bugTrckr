ALTER TABLE `Label` ADD CONSTRAINT `Label_fk0` FOREIGN KEY (`issueId`) REFERENCES `Issue`(`id`);