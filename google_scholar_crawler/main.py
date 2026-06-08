from scholarly import scholarly, ProxyGenerator
import jsonpickle
import json
from datetime import datetime
import os

# Use proxy to avoid Google Scholar blocking
pg = ProxyGenerator()
success = pg.FreeProxies()
if success:
    scholarly.use_proxy(pg)
    print("Proxy enabled successfully")
else:
    print("Warning: Could not enable proxy, trying direct connection...")

author_id = os.environ['GOOGLE_SCHOLAR_ID']
print(f"Fetching data for author ID: {author_id}")

author: dict = scholarly.search_author_id(author_id)
scholarly.fill(author, sections=['basics', 'indices', 'counts', 'publications'])
name = author['name']
print(f"Author: {name}, Cited by: {author.get('citedby', 'N/A')}")

author['updated'] = str(datetime.now())
author['publications'] = {v['author_pub_id']:v for v in author['publications']}
print(json.dumps(author, indent=2))

os.makedirs('results', exist_ok=True)
with open(f'results/gs_data.json', 'w') as outfile:
    json.dump(author, outfile, ensure_ascii=False)

shieldio_data = {
  "schemaVersion": 1,
  "label": "citations",
  "message": f"{author['citedby']}",
}
with open(f'results/gs_data_shieldsio.json', 'w') as outfile:
    json.dump(shieldio_data, outfile, ensure_ascii=False)

print("Citation data saved successfully")
