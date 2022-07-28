import os
from PerformanceWriter import PerformanceWriter

class SplitFileLoadFactor:
    def split(filepath,high_start,high_end,low_start,low_end,res_base_path):
        #split
        lines = ""
        f = open(filepath,'r')
        lines = f.readlines()
        high_lines = lines[high_start:high_end+1]
        low_lines = lines[low_start:low_end+1]
        #write high lf file
        hlf_path = res_base_path + os.sep + "high_lf.txt"
        hlf = open(hlf_path,'w')
        for line in high_lines:
            hlf.write(line)
        #write low fl file
        llf_path = res_base_path + os.sep + "low_lf.txt"
        llf = open(llf_path,"w")
        for line in low_lines:
            llf.write(line)





#run script
#Storage API / Handler
#filepath = r"C:\Users\juanp\OneDrive\Documents\experiment_results\2022-07-09-adaptive\experiments\ca-rt-5400000-ia-50-cy-2700000-cl-1000-st-40\measurements\api.txt"
#high_start = 9
#high_end = 48000*9
#low_start = 57000*9
#low_end = 78000*9
#res_base_path = r"C:\Users\juanp\OneDrive\Documents\experiment_results\2022-07-09-adaptive\experiments\api..."
#SplitFileLoadFactor.split(filepath,high_start,high_end,low_start,low_end,res_base_path)
#get performance metrics
#hlf = res_base_path + os.sep + "high_lf.txt"
#llf = res_base_path + os.sep + "low_lf.txt"
#metrics_results_path = r"C:\Users\juanp\OneDrive\Documents\experiment_results\2022-07-09-adaptive\adaptive-analysis\api..."
#hlf_metrics_path = metrics_results_path + os.sep + "high_lf_metrics.txt"
#llf_metrics_path = metrics_results_path + os.sep + "low_lf_metrics.txt"
#PerformanceWriter.findAndWritePerformanceMetrics(hlf,"STORAGE_API_ENTRY","ENTRY_LIST_EXIT","TRANSMITTER_EXIT",hlf_metrics_path)
#PerformanceWriter.findAndWritePerformanceMetrics(llf,"STORAGE_API_ENTRY","ENTRY_LIST_EXIT","TRANSMITTER_EXIT",llf_metrics_path)

#manager
filepath = r"C:\Users\juanp\OneDrive\Documents\experiment_results\2022-07-09-adaptive\experiments\ca-rt-5400000-ia-50-cy-2700000-cl-1000-st-40\measurements\strgMngr.txt"
high_start = 1
high_end = 5400
low_start = 5500
low_end = 47226
res_base_path = r"C:\Users\juanp\OneDrive\Documents\experiment_results\2022-07-09-adaptive\experiments\mngr_splits"
SplitFileLoadFactor.split(filepath,high_start,high_end,low_start,low_end,res_base_path)

#get performance metrics
hlf = res_base_path + os.sep + "high_lf.txt"
llf = res_base_path + os.sep + "low_lf.txt"
metrics_results_path = r"C:\Users\juanp\OneDrive\Documents\experiment_results\2022-07-09-adaptive\adaptive-analysis\manager_lf_hf"
hlf_metrics_path = metrics_results_path + os.sep + "high_lf_metrics.txt"
llf_metrics_path = metrics_results_path + os.sep + "low_lf_metrics.txt"
PerformanceWriter.findAndWritePerformanceMetrics(hlf,"ENTRY","QueueExitTime","EXIT",hlf_metrics_path)
PerformanceWriter.findAndWritePerformanceMetrics(llf,"ENTRY","QueueExitTime","EXIT",llf_metrics_path)
