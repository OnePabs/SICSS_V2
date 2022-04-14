Run without docker: 
cd .../StorageAPI/server
sudo ./gradlew assemble
sudo ./gradlew run --args="80"

Deploy on docker
cd .../StorageAPI/server
sudo docker build -t storageapi:latest
sudo docker run --network host storageapi:latest