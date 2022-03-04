class PerformanceWriter:

    #@staticmethod
    #def findAndWritePerformanceMetricsOfExperiment(experimentFolderPath,resultFolderPath):


    @staticmethod
    def findAndWritePerformanceMetrics(measurementsPath, entryMeasurementName, serviceTimeEndMeasurementName, exitMeasurementName, resultsPath):
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
                    if data[1] == serviceTimeEndMeasurementName:
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

        


