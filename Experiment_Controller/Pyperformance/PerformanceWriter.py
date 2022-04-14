import os

class PerformanceWriter:

    @staticmethod
    def findAndWritePerformanceMetricsOfExperiment(experimentFolderPath,resultFolderPath):
        list_subfolders_with_paths = [f.path for f in os.scandir(experimentFolderPath) if f.is_dir()]
        #create API results file
        apiResultsPath = resultFolderPath + os.path.sep + "api-results.txt"
        frapi = open(apiResultsPath,"w+")
        frapi.write("Path,Arrival Rate, Time spent in Buffer,Residence Time\n")
        frapi.close()
        #create Manager results file
        managerResultsPath = resultFolderPath + os.path.sep + "manager-results.txt"
        frmanager =  open(managerResultsPath,"w")
        frmanager.write("Path,Arrival Rate,Service Time,Residence Time\n")
        frmanager.close()
        #find and write experiment results from measurement folder
        for path in list_subfolders_with_paths:
            print(path)
            #performance analysis for API
            print("Handler:")
            apiMPath = path + os.path.sep + "measurements" + os.path.sep + "strgapi.txt"
            PerformanceWriter.findAndWritePerformanceMetrics(apiMPath,"STORAGE_API_ENTRY","ENTRY_LIST_EXIT","TRANSMITTER_EXIT",apiResultsPath)
            #performance analysis for Manager
            print("Manager: ")
            managerPath = path + os.path.sep + "measurements" + os.path.sep + "strgMngr.txt"
            PerformanceWriter.findAndWritePerformanceMetrics(managerPath,"ENTRY","QueueExitTime","EXIT",managerResultsPath)
            print()

    @staticmethod
    def findAndWritePerformanceMetrics(measurementsPath, entryMeasurementName, serviceTimeStartMeasurementName, exitMeasurementName, resultsPath):

        #write measurement Path to results path
        fmp = open(resultsPath,"a")
        fmp.write(measurementsPath+",")
        fmp.close()

        #get measurements into python object
        lines = ""
        f = open(measurementsPath, 'r')
        try:
            lines = f.readlines()
        finally:
            f.close()

        #pop first line because it is not measurements
        lines.pop(0)

        #get number of lines
        numberOfLines = len(lines)

        #Get Averate Inter Arrival Time
        #Lines is sorted by time because of how measurements are made at modules
        curline = ""
        data = []
        ias = 0 #sum of inter arrival times
        numIa = 0
        prevIaMeasurement = 0
        currIaMeasurement = 0
        for i in range(0,numberOfLines):
            curline = lines[i]
            if curline and not curline.isspace():
                data = curline.split(",")
                if data[1] == entryMeasurementName:
                    currIaMeasurement = int(data[2])
                    if prevIaMeasurement != 0:
                        ias = ias + (currIaMeasurement - prevIaMeasurement)
                        numIa = numIa + 1
                    prevIaMeasurement = currIaMeasurement

        #Calculate and Write Arrival Rate 
        if numIa != 0:
            averageIa = ias/numIa    
            print("Average ia: " + str(averageIa))  
            print("Num Arrivals: " + str(numIa))
            iaRate = 1000000000/averageIa
            print("Arrival Rate: " + str(iaRate))
            r = open(resultsPath, "a")
            r.write(str(iaRate) + ",")
            r.close()

        #sort list by ID
        lines.sort(key=lambda x: (int(x.split(",")[0]), int(x.split(",")[2])))
        #print(lines[0:6])

        #find average service time and residence time
        stSum = 0
        rsSum = 0
        curId = 0
        curEntry = 0
        curServStart = 0
        curExit = 0
        for i in range(0,numberOfLines):
            curline = lines[i]
            if curline and not curline.isspace():
                data = curline.split(",")
                if data[1] == entryMeasurementName:
                    curId = data[0] #update the current Request Id
                    curEntry = int(data[2])
                elif curId == data[0]:
                    #Working on same request as the current Request Id
                    if data[1] == serviceTimeStartMeasurementName:
                        curServStart = int(data[2])
                    elif data[1] == exitMeasurementName:
                        curExit = int(data[2])
                        stSum = stSum + (curExit-curServStart)
                        rsSum = rsSum + (curExit-curEntry)
                
                #else: Measurement is not an Entry measurement and the Id is not that one of the entry, therefore erroneous
                #skip

        curIdNum = int(curId)
        if curIdNum != 0:
            numCompletedRequests = curIdNum + 1    #ID starts at zero so 1 is added
            avgSt = stSum/numCompletedRequests
            avgSt = avgSt/1000000
            avgRs = rsSum/numCompletedRequests
            avgRs = avgRs/1000000
            print("Average Service Time: " + str(avgSt))
            print("Average Residence Time: " + str(avgRs))
            r2 = open(resultsPath, "a")
            r2.write(str(avgSt) + ","+str(avgRs)+"\n")
            r2.close()

    @staticmethod
    def getAverageNumberOfRequestsInABatch(measurementsPath,thrs):
        lines = ""
        f = open(measurementsPath, 'r')
        try:
            lines = f.readlines()
        finally:
            f.close()
        
        numRequestPerBatchSum = 0
        numBatches = 0
        numRequestsInCurrentBatch = 0
        currentEntryTime = 0
        prevTimeStampName = ""
        for line in lines:
            data = line.split(",")
            if data[1] == "ENTRY" and prevTimeStampName != "ENTRY":
                #new batch
                currentEntryTime = int(data[2]) #set current entry time
            elif data[1] == "ENTRY" and abs(int(data[2]) - currentEntryTime) < thrs:
                #in current entry batch
                numRequestsInCurrentBatch += 1
            elif data[1] != "ENTRY" and prevTimeStampName == "ENTRY":
                #marks the end of an entry batch
                numRequestPerBatchSum += numRequestsInCurrentBatch
                numBatches += 1
                numRequestsInCurrentBatch = 0
            prevTimeStampName = data[1]

        print(measurementsPath)
        print("numRequestPerBatchSum: " + str(numRequestPerBatchSum))
        print("numBatches: " + str(numBatches))
        avg = numRequestPerBatchSum/numBatches
        print("avg: " + str(avg))
        print()


    @staticmethod
    def writeEntryTimes(entryMeasurementName,measurementsPath,resultsPath):
        #get measurements into python object
        lines = ""
        f = open(measurementsPath, 'r')
        try:
            lines = f.readlines()
        finally:
            f.close()

        #pop first line because it is not measurements
        lines.pop(0)

        #get number of lines
        numberOfLines = len(lines)

        #Get and write Arrival Times
        #Lines is already sorted by time because of how measurements are made at modules
        f2 = open(resultsPath,'w')
        curline = ""
        data = []
        for i in range(0,numberOfLines):
            curline = lines[i]
            if curline and not curline.isspace():
                data = curline.split(",")
                if data[1] == entryMeasurementName:
                    f2.write(curline)
        f2.close()
    