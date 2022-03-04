from PerformanceWriter import PerformanceWriter


# expPaths = [
# "C:\\Users\\juanp\\Downloads\\drive-download-20220303T155216Z-001\\A-exp-saturation",
# "C:\\Users\\juanp\\Downloads\\drive-download-20220303T155216Z-001\\B-exp-saturation",
# "C:\\Users\\juanp\\Downloads\\drive-download-20220303T155216Z-001\\C-exp-saturation"
# ]


# resPaths = [
# "C:\\Users\\juanp\\Downloads\\drive-download-20220303T155216Z-001\\A-results",
# "C:\\Users\\juanp\\Downloads\\drive-download-20220303T155216Z-001\\B-results",
# "C:\\Users\\juanp\\Downloads\\drive-download-20220303T155216Z-001\\C-results"
# ]

# numExperiments = len(expPaths)

# for i in range(numExperiments):
#     PerformanceWriter.findAndWritePerformanceMetricsOfExperiment(expPaths[i],resPaths[i])


path = "C:\\Users\\juanp\\Documents\\experiment_results\\old\\B-exp-periods\\B-appIdx-0apiIdx-0rt-2700000st-40p-5ia-50\\measurements\\strgMngr.txt"
PerformanceWriter.getAverageNumberOfRequestsInABatch(path,50)