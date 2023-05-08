import torchgeo.datasets as dg

data_dir = 'C:/Users/shami/OneDrive/Desktop/Spacenet6'
api_key = 'b32ef704be9b3092cfd0a64b34b675f39958ca6aa8b14408ed549ac9c740aa22'

dataset = dg.SpaceNet6(
    root=data_dir,
    api_key=api_key,
    download=True
)
