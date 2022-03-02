from PerformanceWriter import PerformanceWriter

listOfMeasurementFolders = [
    "C:\\Users\\juanp\\Documents\\experiment_results\\B-exp-periods\\B-appIdx-0apiIdx-0rt-2700000st-40p-40ia-50\\measurements",
    "C:\\Users\\juanp\\Documents\\experiment_results\\B-exp-periods\\B-appIdx-0apiIdx-0rt-2700000st-40p-50ia-50\\measurements",
    "C:\\Users\\juanp\\Documents\\experiment_results\\B-exp-periods\\B-appIdx-0apiIdx-0rt-2700000st-40p-250ia-50\\measurements",
    "C:\\Users\\juanp\\Documents\\experiment_results\\B-exp-periods\\B-appIdx-0apiIdx-0rt-2700000st-40p-1500ia-50\\measurements"
]

### Compute Cloud parameters ###
entryMeasurementNameHandler = "STORAGE_API_ENTRY"
serviceTimeStartMeasurementNameHandler = "ENTRY_LIST_EXIT"
exitMeasurementNameHandler = "TRANSMITTER_EXIT"
resultsPathHandler = "C:\\Users\\juanp\\Documents\\experiment_results\\resultsHandler.txt"

### Storage Cloud Parameters ###
entryMeasurementNameManager = "ENTRY"
serviceTimeStartMeasurementNameManager = "QueueExitTime"
exitMeasurementNameManager = "EXIT"
resultsPathManager = "C:\\Users\\juanp\\Documents\\experiment_results\\resultsManager.txt"

for path in listOfMeasurementFolders:
    print(path)
    print("Compute Cloud")
    PerformanceWriter.findAndWritePerformanceMetrics(path+"\\strgapi.txt",entryMeasurementNameHandler,serviceTimeStartMeasurementNameHandler,exitMeasurementNameHandler,resultsPathHandler)
    print("Storage cloud")
    PerformanceWriter.findAndWritePerformanceMetrics(path+"\\strgMngr.txt",entryMeasurementNameManager,serviceTimeStartMeasurementNameManager,exitMeasurementNameManager,resultsPathManager)
    print()