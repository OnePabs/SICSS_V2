from PerformanceWriter import PerformanceWriter


listOfStorageManagerMeasurementsPaths = {
    # "C:\\Users\\juanp\\Documents\\experiment_results\\B-exp-periods\\B-appIdx-0apiIdx-0rt-2700000st-40p-5ia-50\\measurements\\strgMngr.txt",
    # "C:\\Users\\juanp\\Documents\\experiment_results\\B-exp-periods\\B-appIdx-0apiIdx-0rt-2700000st-40p-20ia-50\\measurements\\strgMngr.txt",
    # "C:\\Users\\juanp\\Documents\\experiment_results\\B-exp-periods\\B-appIdx-0apiIdx-0rt-2700000st-40p-100ia-50\\measurements\\strgMngr.txt",
    # "C:\\Users\\juanp\\Documents\\experiment_results\\B-exp-periods\\B-appIdx-0apiIdx-0rt-2700000st-40p-200ia-50\\measurements\\strgMngr.txt",
    # "C:\\Users\\juanp\\Documents\\experiment_results\\B-exp-periods\\B-appIdx-0apiIdx-0rt-2700000st-40p-500ia-50\\measurements\\strgMngr.txt"
    "C:\\Users\\juanp\\Documents\\experiment_results\\B-exp-saturation\\B-appIdx-0apiIdx-0rt-2700000st-40p-1000ia-50\\measurements\\strgMngr.txt"
}

entryMeasurementNameManager = "ENTRY"
serviceTimeStartMeasurementNameManager = "QueueExitTime"
exitMeasurementNameManager = "EXIT"
resultsPathManager = "C:\\Users\\juanp\\Documents\\experiment_results\\resultsManager.txt"

for storageManagerMeasurementPath in listOfStorageManagerMeasurementsPaths:
    print(storageManagerMeasurementPath)
    PerformanceWriter.findAndWritePerformanceMetrics(storageManagerMeasurementPath,entryMeasurementNameManager,serviceTimeStartMeasurementNameManager,exitMeasurementNameManager,resultsPathManager)
    print()

listOfStorageHandlerPaths = [
    "C:\\Users\\juanp\\Documents\\experiment_results\\B-exp-saturation\\B-appIdx-0apiIdx-0rt-2700000st-40p-1000ia-50\\measurements\\strgapi.txt"
    #"C:\\Users\\juanp\\Documents\\experiment_results\\B-exp-periods\\B-appIdx-0apiIdx-0rt-2700000st-40p-5ia-50\\measurements\\strgapi.txt",
    #"C:\\Users\\juanp\\Documents\\experiment_results\\B-exp-periods\\B-appIdx-0apiIdx-0rt-2700000st-40p-20ia-50\\measurements\\strgapi.txt",
    #"C:\\Users\\juanp\\Documents\\experiment_results\\B-exp-periods\\B-appIdx-0apiIdx-0rt-2700000st-40p-100ia-50\\measurements\\strgapi.txt",
    #"C:\\Users\\juanp\\Documents\\experiment_results\\B-exp-periods\\B-appIdx-0apiIdx-0rt-2700000st-40p-200ia-50\\measurements\\strgapi.txt",
    #"C:\\Users\\juanp\\Documents\\experiment_results\\B-exp-periods\\B-appIdx-0apiIdx-0rt-2700000st-40p-500ia-50\\measurements\\strgapi.txt"
]

entryMeasurementNameHandler = "STORAGE_API_ENTRY"
serviceTimeStartMeasurementNameHandler = "ENTRY_LIST_EXIT"
exitMeasurementNameHandler = "TRANSMITTER_EXIT"
resultsPathHandler = "C:\\Users\\juanp\\Documents\\experiment_results\\resultsHandler.txt"

for storageHandlerPath in listOfStorageHandlerPaths:
    print(storageHandlerPath)
    PerformanceWriter.findAndWritePerformanceMetrics(storageHandlerPath,entryMeasurementNameHandler,serviceTimeStartMeasurementNameHandler,exitMeasurementNameHandler,resultsPathHandler)
    print()