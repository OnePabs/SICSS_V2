from PerformanceWriter import PerformanceWriter


expPaths = [
#"C:\\Users\\Juan Pablo Contreras\\Documents\\expresults\\results"
#"C:\\Users\\juanp\\Documents\\experiment_results\\measurements"
r"C:\Users\juanp\Downloads\TechniqueBConstant\TechniqueBConstant"
]


resPaths = [
#"C:\\Users\\Juan Pablo Contreras\\Documents\\expresults\\analysis"
r"C:\Users\juanp\OneDrive\Documents\expresults\analysis"
]

numExperiments = len(expPaths)

for i in range(numExperiments):
    PerformanceWriter.findAndWritePerformanceMetricsOfExperiment(expPaths[i],resPaths[i])
    print()


#path = "C:\\Users\\juanp\\Documents\\experiment_results\\old\\B-exp-periods\\B-appIdx-0apiIdx-0rt-2700000st-40p-5ia-50\\measurements\\strgMngr.txt"
#PerformanceWriter.getAverageNumberOfRequestsInABatch(path,50)

#path = "C:\\Users\\juanp\\Documents\\experiment_results\\C-exp-saturation\\C-app-0-api-0-ia-100-mx-2000st-40\\measurements\\strgapi.txt"
#resultPath = "C:\\Users\juanp\\Documents\\experiment_results\\c-analysis\\entries.txt"
#PerformanceWriter.writeEntryTimes("STORAGE_API_ENTRY",path,resultPath)

